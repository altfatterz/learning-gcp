 ```
 gcloud container images  list --repository eu.gcr.io/learning-messaging
 gcloud auth list
 gcloud config set core/project learning-messaging
 ```
 
 ```
 gcloud services list --enabled
 gcloud services list --available
 ```

 ERROR: (gcloud.container.images.list) Bad status during token exchange: 403
 {"errors":[{"code":"DENIED","message":"Token exchange failed for project 'learning-messaging'. Please enable or contact project owners to enable the Google Container Registry API in Cloud Console at https://console.cloud.google.com/apis/api/containerregistry.googleapis.com/overview?project=learning-messaging before performing this operation."}]}
 
Enable a service:

```bash
$ gcloud services enable containerregistry.googleapis.com
```

Enable Cloud Build:
```bash
$ gcloud services enable cloudbuild.googleapis.com
```

Using a Jib Build
```bash
$ ./mvnw clean install jib:build -Dimage=eu.gcr.io/learning-messaging/cloud-pubsub-demo
```


```bash
$ gcloud builds submit
```

```bash
$ skaffold diagnose
```


###Continuous Development
 
Start up your local Kubernetes cluster with Docker Deskop 
```
$ skaffold dev
```

```bash
$ kubectl get all
```

```bash
NAME                                                READY   STATUS    RESTARTS   AGE
pod/cloud-pubsub-demo-deployment-5df5f687bd-2nqrl   1/1     Running   0          5m58s
pod/cloud-pubsub-demo-deployment-5df5f687bd-kl7p8   1/1     Running   0          5m58s
pod/cloud-pubsub-demo-deployment-5df5f687bd-wbqct   1/1     Running   0          5m58s

NAME                        TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
service/cloud-pubsub-demo   NodePort    10.101.173.199   <none>        8080:30001/TCP   5m58s
service/kubernetes          ClusterIP   10.96.0.1        <none>        443/TCP          3d21h

NAME                                           READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/cloud-pubsub-demo-deployment   3/3     3            3           5m58s

NAME                                                      DESIRED   CURRENT   READY   AGE
replicaset.apps/cloud-pubsub-demo-deployment-5df5f687bd   3         3         3       5m58s
```

Testing
```bash
echo hello | http post :30001/messags
```


## Cloud PubSub

Testing locally after starting the application..

```bash
$ echo hello | http post :8080/messages
```

```bash
$ gcloud pubsub subscriptions pull demo-subscription
```

```bash
┌───────┬──────────────────┬────────────┬──────────────────┬────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
│  DATA │    MESSAGE_ID    │ ATTRIBUTES │ DELIVERY_ATTEMPT │                                                                                           ACK_ID                                                                                           │
├───────┼──────────────────┼────────────┼──────────────────┼────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
│ hello │ 1169749855281299 │            │                  │ IT4wPkVTRFAGFixdRkhRNxkIaFEOT14jPzUgKEUSCQpPAihdeTFYPkFVcWhRDRlyfWByaF8WCAUQWiwJURsHaE5tdSVxDBh0dGZxY1IWBABNUnxWUjPb3O6BpMDoPwNOReq94pwmIfPxi81tZiU9XhJLLD5-IDBFQV5AEkwrBURJUytDCypYEU4EIQ │
└───────┴──────────────────┴────────────┴──────────────────┴────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
```

If it is not acknowledged that it will be redelivered with the subscription.

```bash
$ gcloud pubsub subscriptions pull --auto-ack demo-subscription
┌───────┬──────────────────┬────────────┬──────────────────┐
│  DATA │    MESSAGE_ID    │ ATTRIBUTES │ DELIVERY_ATTEMPT │
├───────┼──────────────────┼────────────┼──────────────────┤
│ hello │ 1169749855281299 │            │                  │
└───────┴──────────────────┴────────────┴──────────────────┘
$ gcloud pubsub subscriptions pull --auto-ack demo-subscription
Listed 0 items.
```

`GcpPubSubAutoConfiguration` for auto configuration. 

### JSON support

We configure a `PubSubMessageConverter` Here we rely on the ObjectMapper provided by Spring Boot.

@Bean
public PubSubMessageConverter pubSubMessageConverter(ObjectMapper objectMapper) {
    return new JacksonPubSubMessageConverter(objectMapper);
}

```bash
$ echo '{"deviceId":"12345", "temperature": 25.15}'  | http post :8080/measurements
``` 

You won't get the messages if the subscription is created after the message was published to the topic.

### Subscription

```bash
http :8080/messages\?subscription=messages-subscription\&maxMessages=10
```


# Separate subscriber from publisher (use separate service accounts)
How to externalise the service account and use it a secret when running on GKE?


### Snapshots

Snaphosts provide a safety-net allowing you to quickly rool back to a point in time and replay messages.
It comes handy when you want to deploy the new version of your application but you are concerned that it may need 
to be rolled back and you want to preserve any Pub/Sub messages it may process in the meantime.
You could create a test topic and roll out the application changes against that topic, but that would require to populate
that topic and would not allow you to test your changes on production traffic.
 
### Testing locally with emulator

```bash
$ gcloud components install pubsub-emulator
$ gcloud components update
```

```bash
$ gcloud beta emulators pubsub start --project=not-real-project-id
Executing: /usr/local/Caskroom/google-cloud-sdk/latest/google-cloud-sdk/platform/pubsub-emulator/bin/cloud-pubsub-emulator --host=localhost --port=8085
[pubsub] This is the Google Pub/Sub fake.
[pubsub] Implementation may be incomplete or differ from the real system.
[pubsub] May 27, 2020 6:22:26 PM com.google.cloud.pubsub.testing.v1.Main main
[pubsub] INFO: IAM integration is disabled. IAM policy methods and ACL checks are not supported
[pubsub] May 27, 2020 6:22:27 PM io.gapi.emulators.netty.NettyUtil applyJava7LongHostnameWorkaround
[pubsub] INFO: Unable to apply Java 7 long hostname workaround.
[pubsub] May 27, 2020 6:22:27 PM com.google.cloud.pubsub.testing.v1.Main main
[pubsub] INFO: Server started, listening on 8085
```

In order to create topics and subscriptions we need to have an application using the Google Cloud Client libraries. 
The emulator does not support the `gcloud pubsub` commands.

In our example we use Spring support using `PubSubAdmin` with following `local` profile:

```yaml
spring:
  profiles: local
  cloud:
    gcp:
      pubsub:
        emulator-host: localhost:8085
```

```bash
$ http post :8080/topics\?topicName=greetings
```
This topic will be created locally.

Verify here that when running against Cloud Pub/Sub the service account will not allow this.

We get the following error message:

```bash
io.grpc.StatusRuntimeException: PERMISSION_DENIED: User not authorized to perform this action.
```

Since our service account included only `Pub/Sub Publisher` and `Pub/Sub Subscriber` roles.

Now we can publish:

```bash
echo "new message" | http post :8080/messages
```



More details here [https://cloud.google.com/pubsub/docs/emulator#pubsub-emulator-java](https://cloud.google.com/pubsub/docs/emulator#pubsub-emulator-java)
