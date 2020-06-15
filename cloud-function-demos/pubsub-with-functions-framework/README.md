```bash
$ gcloud functions deploy pubsub-with-functions-framework \
  --entry-point com.example.PubSubGreeter \
  --trigger-topic demo-topic \
  --runtime java11 \
  --allow-unauthenticated \
  --memory 512MB   
```



List Pub/Sub topics:

```bash
$ gcloud pubsub topics list
---
name: projects/learning-gcp-278815/topics/demo-topic
```


