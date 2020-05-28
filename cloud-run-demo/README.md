## Cloud Run 

Cloud Run is available in the following regions:

* asia-east1 (Taiwan)
* asia-northeast1 (Tokyo)
* europe-north1 (Finland)
* europe-west1 (Belgium)
* europe-west4 (Netherlands)
* us-central1 (Iowa)
* us-east1 (South Carolina)
* us-east4 (Northern Virginia)
* us-west1 (Oregon)

More details here [https://cloud.google.com/run/docs/locations](https://cloud.google.com/run/docs/locations)

### Setup

```bash
$ gcloud config set run/platform managed
$ gcloud config set run/region europe-west1
```

# Cloud Build

```bash
$ gcloud builds submit

ID                                    CREATE_TIME                DURATION  SOURCE                                                                                        IMAGES  STATUS
f802d843-132c-4c0d-8244-cf8c6007ad88  2020-05-28T13:12:31+00:00  59S       gs://learning-messaging_cloudbuild/source/1590671513.34-5469e235082b42f9886d4630d6898512.tgz  -       SUCCESS
```

This will create the Docker image into `eu.gcr.io/learning-messaging/cloud-build-demo`

### Cloud Run Deploy

```bash
⇒  gcloud run deploy cloud-run-demo --image=eu.gcr.io/learning-messaging/cloud-run-demo --allow-unauthenticated
Deploying container to Cloud Run service [cloud-run-demo] in project [learning-messaging] region [europe-west1]
✓ Deploying... Done.
  ✓ Creating Revision...
  ✓ Routing traffic...
  ✓ Setting IAM Policy...
Done.
Service [cloud-run-demo] revision [cloud-run-demo-00002-xin] has been deployed and is serving 100 percent of traffic at https://cloud-run-demo-udypuxt5sq-ew.a.run.app
```

### Cloud Run services list

```bash
$ gcloud run services list
SERVICE         REGION        URL                                             LAST DEPLOYED BY                       LAST DEPLOYED AT
cloud-run-demo  europe-west1  https://cloud-run-demo-udypuxt5sq-ew.a.run.app  zoltan.altfatter@cloudnativecoach.com  2020-05-28T13:31:07.134Z
```

### Cloud Run services describe

```bash
$ gcloud run services describe cloud-run-demo
✔ Service cloud-run-demo in region europe-west1

Traffic: https://cloud-run-demo-udypuxt5sq-ew.a.run.app
  100% LATEST (currently cloud-run-demo-00003-zal)

Last updated on 2020-05-28T13:31:07.134Z by zoltan.altfatter@cloudnativecoach.com:
  Revision cloud-run-demo-00003-zal
  Image:         eu.gcr.io/learning-messaging/cloud-run-demo
  Port:          8080
  Memory:        256M
  CPU:           1000m
  Concurrency:   80
  Max Instances: 1000
  Timeout:       900s
```

### View the revisions

```bash
$ gcloud run revisions list
REVISION                  ACTIVE  SERVICE         DEPLOYED                 DEPLOYED BY
cloud-run-demo-00003-zal  yes     cloud-run-demo  2020-05-28 13:30:39 UTC  zoltan.altfatter@cloudnativecoach.com
cloud-run-demo-00002-xin          cloud-run-demo  2020-05-28 13:14:32 UTC  zoltan.altfatter@cloudnativecoach.com
cloud-run-demo-00001-nij          cloud-run-demo  2020-05-28 13:03:56 UTC  zoltan.altfatter@cloudnativecoach.com
```



