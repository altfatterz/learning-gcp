Running locally

```bash
$ mvn function:run
```

Deploy 

```bash
$ gcloud functions deploy http-with-functions-framework \
    --region=europe-west3 \
    --entry-point=com.example.Greeter \
    --runtime=java11 \
    --allow-unauthenticated \
    --trigger-http 
    
Deploying function (may take a while - up to 2 minutes)...done.
availableMemoryMb: 256
entryPoint: com.example.Greeter
httpsTrigger:
  url: https://europe-west3-learning-gcp-278815.cloudfunctions.net/http-with-functions-framework
ingressSettings: ALLOW_ALL
labels:
  deployment-tool: cli-gcloud
name: projects/learning-gcp-278815/locations/europe-west3/functions/http-with-functions-framework
runtime: java11
serviceAccountEmail: learning-gcp-278815@appspot.gserviceaccount.com
sourceUploadUrl: https://storage.googleapis.com/gcf-upload-europe-west3-0c38a045-6da5-425e-96bd-23183f660385/99b339c5-8798-4f13-9cea-e4bc0ade381e.zip?GoogleAccessId=service-844536204453@gcf-admin-robot.iam.gserviceaccount.com&Expires=1592255734&Signature=kZiKxqrV%2FYNNHnCidtZjhpV6vXN0QIdVejvJdf1P%2B7J6TM0yO09dd8k%2FGppdmf0t4uj%2B09jprjCZfjWxQI1LIpn6KeRWOIi2XTGMQTR93YGHLmprbX%2FzZb%2Fx6bmAZNcJxwh5j6FDM3tPZ80164LCfHAGGrodfuiQ06l17XQMXJIBVcdMNSBU2Uw6%2Fahnx2QM98linT%2BuWzvVrrHLfntD5N9YXoBfJl%2FfoZPqg4990YN%2FClnE9qIZ7uDUU%2FTSaTTL5Ntp4KbLJ9tBGiSIobmfVU5dzySiRRwRneqmTF3laQpRCqiQgoC0wZGicUwxckjzXzsMk2AAQD%2B261IKcoKC6g%3D%3D
status: ACTIVE
timeout: 60s
updateTime: '2020-06-15T20:48:02.751Z'
versionId: '1
```

When you deploy a function for the first time, you must include the `--runtime` flag. The `--runtime` flag is not required on subsequent deployments of the function.



Listing the deployed functions

```bash
$ gcloud functions list

NAME                                     STATUS  TRIGGER       REGION
http-with-functions-framework  ACTIVE  HTTP Trigger  europe-west3
```

Let's call the deployed function

```bash
$ http https://europe-west3-learning-gcp-278815.cloudfunctions.net/http-with-functions-framework

HTTP/1.1 200 OK
Alt-Svc: h3-27=":443"; ma=2592000,h3-25=":443"; ma=2592000,h3-T050=":443"; ma=2592000,h3-Q050=":443"; ma=2592000,h3-Q049=":443"; ma=2592000,h3-Q048=":443"; ma=2592000,h3-Q046=":443"; ma=2592000,h3-Q043=":443"; ma=2592000,quic=":443"; ma=2592000; v="46,43"
Content-Length: 12
Content-Type: text/plain; charset=utf-8
Date: Mon, 15 Jun 2020 20:50:35 GMT
Function-Execution-Id: 218s02iain7n
Server: Google Frontend
X-Cloud-Trace-Context: f59eeb485b6a9354fcce3756f04df64f;o=1

Hello World!

```

Get the logs

```bash
$ gcloud functions logs read http-with-functions-framework
...
D      http-with-functions-framework  n9kvibv69ns3  2020-06-15 20:22:14.023  Function execution started
D      http-with-functions-framework  n9kvibv69ns3  2020-06-15 20:22:14.041  Function execution took 19 ms, finished with status code: 200
```

Delete the function

```bash
$ gcloud functions delete http-with-functions-framework
```