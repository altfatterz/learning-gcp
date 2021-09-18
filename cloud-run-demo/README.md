### Setup

Create a project `cloud-run-demo` and set up your `gcloud` CLI to use it.


```bash
$ export PROJECT_ID=$(gcloud info --format='value(config.project)')
$ gcloud config set project PROJECT_ID
```

Enable `Cloud Build` and `Cloud Run` service. 

```bash
$ gcloud services enable cloudbuild.googleapis.com
$ gcloud services enable run.googleapis.com
```

To view already enabled services use:

```bash
$ gcloud services list
```

To view available services use:
```
$ gcloud services list --available
```

## Cloud Run 

Cloud Run is available in the following regions:

```bash
$ gcloud run regions --list
```

### Setup

```bash
$ gcloud config set run/platform managed
$ gcloud config set run/region europe-west6
```

# Cloud Build

```bash
$ gcloud builds submit
```

We also cache the maven dependencies into `Cloud Storage`, so subsequent builds are a bit faster:

```bash
$ gcloud builds list

08185db1-ae1d-4905-9123-79ca9518c774  2021-09-18T16:49:33+00:00  40S       gs://cloud-run-demo-326416_cloudbuild/source/1631983770.438572-c11bba2d86314adda35296cd0ea7a9ae.tgz  -       SUCCESS
e023fd35-71cf-4177-8fa8-fab60b9dff67  2021-09-18T16:48:14+00:00  1M4S      gs://cloud-run-demo-326416_cloudbuild/source/1631983691.635307-3ae2677839554c7c8ced138d250a6a9f.tgz  -       SUCCESS
```

This will create the Docker image into `eu.gcr.io/${PROJECT_ID}/cloud-build-demo`

### Cloud Run Deploy

```bash
$ gcloud run deploy cloud-run-demo --image=eu.gcr.io/${PROJECT_ID}/cloud-run-demo --allow-unauthenticated
```

```bash
Deploying container to Cloud Run service [cloud-run-demo] in project [cloud-run-demo-326416] region [europe-west6]
✓ Deploying new service... Done.
  ✓ Creating Revision...
  ✓ Routing traffic...
  ✓ Setting IAM Policy...
Done.
Service [cloud-run-demo] revision [cloud-run-demo-00001-rog] has been deployed and is serving 100 percent of traffic.
Service URL: https://cloud-run-demo-vqh5wju2aq-oa.a.run.app
```

### Cloud Run services list

```bash
$ gcloud run services list
```

```
SERVICE         REGION        URL                                             LAST DEPLOYED BY                       LAST DEPLOYED AT
✔  cloud-run-demo  europe-west6  https://cloud-run-demo-vqh5wju2aq-oa.a.run.app  zoltan.altfatter@cloudnativecoach.com  2021-09-18T16:53:44.364107Z
```

### Cloud Run services describe

```bash
$ gcloud run services describe cloud-run-demo
✔ Service cloud-run-demo in region europe-west6

URL:     https://cloud-run-demo-vqh5wju2aq-oa.a.run.app
Ingress: all
Traffic:
  100% LATEST (currently cloud-run-demo-00001-rog)

Last updated on 2021-09-18T16:53:44.364107Z by zoltan.altfatter@cloudnativecoach.com:
  Revision cloud-run-demo-00001-rog
  Image:         eu.gcr.io/cloud-run-demo-326416/cloud-run-demo
  Port:          8080
  Memory:        512Mi
  CPU:           1000m
  Concurrency:   80
  Max Instances: 100
  Timeout:       300s
altfatterz@zoltan-altfa
```

### View the revisions

```bash
$ gcloud run revisions list
REVISION                  ACTIVE  SERVICE         DEPLOYED                 DEPLOYED BY
cloud-run-demo-00003-zal  yes     cloud-run-demo  2020-05-28 13:30:39 UTC  zoltan.altfatter@cloudnativecoach.com
cloud-run-demo-00002-xin          cloud-run-demo  2020-05-28 13:14:32 UTC  zoltan.altfatter@cloudnativecoach.com
cloud-run-demo-00001-nij          cloud-run-demo  2020-05-28 13:03:56 UTC  zoltan.altfatter@cloudnativecoach.com
```

### Run the application:

```bash
$ curl https://cloud-run-demo-vqh5wju2aq-oa.a.run.app
Hello World!
```

### Check logs in Logs Explorer





