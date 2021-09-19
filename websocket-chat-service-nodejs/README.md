Create a project

```bash
$ export PROJECT_ID=websocket-chat-service-nodejs
$ export REGION=europe-west6
$ gcloud projects create $PROJECT_ID
```

Enable billing:

Get the billing account id:
```bash
$ gcloud beta billing accounts list
$ gcloud alpha billing projects link $PROJECT_ID --billing-account 0X0X0X-0X0X0X-0X0X0X
```

Configure `gcloud`: 

```bash
$ gcloud config set project $PROJECT_ID
$ gcloud config set run/region $REGION
```

Enable needed APIs:

```bash
$ gcloud services enable cloudbuild.googleapis.com
$ gcloud services enable run.googleapis.com
$ gcloud services enable redis.googleapis.com
$ gcloud services enable vpcaccess.googleapis.com
$ gcloud services enable artifactregistry.googleapis.com
```

View enabled services:

```bash
$ gcloud services list
```

Create a Memorystore for Redis instance:

```bash
$ gcloud redis instances create my-redis-instance --size=1 --region=$REGION
$ gcloud redis instances list --region $REGIO
```

Set up a Serverless VPC Access connector:

```bash
$ gcloud compute networks vpc-access connectors create my-vpc-connector \
  --region $REGION \
  --range "10.8.0.0/28"
  
$ gcloud compute networks vpc-access connectors list --region=$REGION  
```

```bash
$ gcloud compute networks vpc-access connectors list --region=$REGION 
```

Get the Redis IP

```bash
$ export REDISHOST=$(gcloud redis instances describe my-redis-instance --region $REGION --format "value(host)")
```

Create a service account to serve as the service identity. By default this has no privileges other than project 
membership. This service does not need to interact with anything else in Google Cloud; therefore no additional
permissions need to be assigned to this service account.

```bash
$ gcloud iam service-accounts create chat-identity
```

Deploy to Cloud Run

```bash
$ gcloud run deploy chat-app --source . \
    --vpc-connector my-vpc-connector \
    --allow-unauthenticated \
    --timeout 3600 \
    --service-account chat-identity \
    --update-env-vars REDISHOST=$REDISHOST
```

### Cleanup

```bash
$ gcloud projects delete $PROJECT_ID
```


