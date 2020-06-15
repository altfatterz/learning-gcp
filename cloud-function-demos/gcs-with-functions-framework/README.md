```bash
$ gcloud functions deploy java-hello-gcs \
    --entry-point com.example.GCSGreeter \
    --runtime java11 \
    --memory 512MB \
    --trigger-bucket demo-bucket
```
