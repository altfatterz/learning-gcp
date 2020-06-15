Running locally

```bash
$ mvn function:run
```

Deploy 

```bash
$ gcloud functions deploy cloud-function-with-functions-framework --entry-point com.example.Greeter --runtime java11 --trigger-http --allow-unauthenticated

Deploying function (may take a while - up to 2 minutes)...done.
availableMemoryMb: 256
entryPoint: com.example.Greeter
httpsTrigger:
  url: https://us-central1-learning-gcp-278815.cloudfunctions.net/cloud-function-with-functions-framework
ingressSettings: ALLOW_ALL
labels:
  deployment-tool: cli-gcloud
name: projects/learning-gcp-278815/locations/us-central1/functions/cloud-function-with-functions-framework
runtime: java11
serviceAccountEmail: learning-gcp-278815@appspot.gserviceaccount.com
sourceUploadUrl: https://storage.googleapis.com/gcf-upload-us-central1-021e7c5c-a520-4266-8ef2-1196dfa1f5b6/a81eaf11-a2f8-42c2-a2d0-3ffe95a9ff78.zip?GoogleAccessId=service-844536204453@gcf-admin-robot.iam.gserviceaccount.com&Expires=1592253768&Signature=x3i8u8GqUbhYMwhmffkZ5og3gdXf93d1olN0X4z0KCPPcz%2Fl15TH212m7UPFysxiQuBdp%2Fuk0DBVszqx1XKB5B55qbuvWVB%2BCWt7LtwvNUPoV16mSzNKcXOlnk%2Bckrue4ZLJLCMTMsaA3A7%2BoTs5uN6h887VbzG9z%2BK2q96UjacvyktucSjOMRiJI6ciD1d9CFg%2BOe0yIt%2F2ZaKBNNEBz6fir%2B%2BP3sWzL0h4Y%2FpDv%2BCYBCfGv6bnqv%2F%2F2a72E841Qkfo2p93PaQPnHdAuoY%2BOuKDdpRPh5Q3K6KWwdyNs17ya6FLBcfzc2XFbOSr20Y5RmIoasaP37gnYEdqISy1NA%3D%3D
status: ACTIVE
timeout: 60s
updateTime: '2020-06-15T20:14:51.642Z'
versionId: '1'
```

When you deploy a function for the first time, you must include the --runtime flag. The --runtime flag is not required on subsequent deployments of the function.

Listing the deployed functions

```bash
$ gcloud functions list

NAME                                     STATUS  TRIGGER       REGION
cloud-function-with-functions-framework  ACTIVE  HTTP Trigger  us-central1
```

Let's call the deployed function

```bash
$ http  https://us-central1-learning-gcp-278815.cloudfunctions.net/cloud-function-with-functions-framework

HTTP/1.1 200 OK
Alt-Svc: h3-27=":443"; ma=2592000,h3-25=":443"; ma=2592000,h3-T050=":443"; ma=2592000,h3-Q050=":443"; ma=2592000,h3-Q049=":443"; ma=2592000,h3-Q048=":443"; ma=2592000,h3-Q046=":443"; ma=2592000,h3-Q043=":443"; ma=2592000,quic=":443"; ma=2592000; v="46,43"
Content-Length: 12
Content-Type: text/plain; charset=utf-8
Date: Mon, 15 Jun 2020 20:15:57 GMT
Function-Execution-Id: n9kv1uea5vsi
Server: Google Frontend
X-Cloud-Trace-Context: 1cec83c5f854e19a2a1294cb360c8262;o=1

Hello World!
```

Get the logs

```bash
$ gcloud functions logs read cloud-function-with-functions-framework
...
D      cloud-function-with-functions-framework  n9kvibv69ns3  2020-06-15 20:22:14.023  Function execution started
D      cloud-function-with-functions-framework  n9kvibv69ns3  2020-06-15 20:22:14.041  Function execution took 19 ms, finished with status code: 200
```

 
