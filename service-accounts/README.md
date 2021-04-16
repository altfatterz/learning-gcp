`User Account`
- Has email/password
- User accounts should never be shared
- has IAM Roles, which gives him permissions to do stuff
- can be `enabled` or `disabled`

`Service Account` 
- An identity that a `compute-instance` or an `application` can use to run API requests on your behalf.
- Users can also `impersonate service accounts`, allowing multiple users to access everything that a service account can access.  
- Has an assigned email
- Can only be authenticated with a private-public RSA key pair
- Can't be logged in via a browser
- has IAM Roles, just like the `User Accounts` - you need to be extra careful when you edit them, since different processes depend on them 

`Google-managed Service Account`
- They might sometimes be referred as `service agents` 
- Service Accounts created and managed by Google for many Google Cloud services to run internal Google processes on your behalf

```bash
$ gcloud projects get-iam-policy service-accounts-demo-310307
bindings:
- members:
  - serviceAccount:service-763832670001@compute-system.iam.gserviceaccount.com
  role: roles/compute.serviceAgent
- members:
  - serviceAccount:763832670001-compute@developer.gserviceaccount.com
  - serviceAccount:763832670001@cloudservices.gserviceaccount.com
  role: roles/editor
- members:
  - user:zoltan.altfatter@cloudnativecoach.com
  role: roles/owner
```

`User-managed Service Account`
- You create explicitly using IAM
- We choose a name the roles associated

`Default Service Account`
- These are user managed service accounts that are created when you enable a service that are meant to help you to get started
- Are generally granted the `editor role` which is too broad for production usage


Display all user managed service accounts:

```bash
$ gcloud iam service-accounts list
DISPLAY NAME                            EMAIL                                               DISABLED
Compute Engine default service account  763832670001-compute@developer.gserviceaccount.com  False
```

Display all service accounts including Google managed ones:
This includes also user accounts, user-managed service accounts and google managed service accounts as long as they have roles assigned to them.

```bash
$ gcloud projects get-iam-policy <project-id>
```

Display all projects:

```bash
$ gcloud projects list

PROJECT_ID                    NAME                   PROJECT_NUMBER
cloud-functions-demo-305519   cloud-functions-demo   86580218370
cloud-run-demos-306217        cloud-run-demos        876276068653
service-accounts-demo-310307  service-accounts-demo  763832670001
```


Creating service accounts:
- by default it will have no roles assigned to them 
- include a descriptive display name and description, to describe the purpose of the service account
```bash
$ gcloud iam service-accounts create NAME 
--display-name 'DISPLAY_NAME'
--description 'DESCRIPTION'
```

```bash
$ gcloud iam service-account describe <SERVICE-ACCOUNT> 
```

Is recommended that before deleting, first disable the service account to check if other processes are not needing it.

```bash
$ gcloud iam service-account disable <SERVICE-ACCOUNT>
$ gcloud iam service-account enable <SERVICE-ACCOUNT>
$ gcloud iam service-account disable <SERVICE-ACCOUNT>
```

In addition to the project's IAM policy there is an IAM policy for each service account in your project.

Service Account Roles:
- `roles/iam.serviceAccountCreator`
    - Access to create service accounts.

- `roles/iam.serviceAccountDeleter`
    -  Access to delete service accounts.
  
- `roles/iam.serviceAccountUser`  
    -  Run operations as the service account.

- `roles/iam.serviceAccountAdmin` 
    - Create and manage service accounts, even update their iam policy
    - User of this role can delegate access service accounts to other users

- `roles/iam.serviceAccountKeyAdmin`
    - Users of this role will be able to create, update and delete service account keys.
    - These keys can be used by other users to run commands as a corresponding service account

- `roles/iam.serviceAccountTokenCreator`
    - With this role a user is able to use the impersonate-service-account flag to run gcloud commands as a specified service account
    - This impersonation does not require any private key and is considered a safer alternative
    
Service accounts aren't just accounts they are also considered as resources with an attached IAM policy.

```bash
$ gcloud iam service-accounts get-iam-policy SERVICE_ACCOUNT 
```
A user can have indirect access to a service account through another service account.

1. Service account keys

```bash
$ gcloud iam service-accounts keys create OUTPUT_PRIVATE_KEY_FILE --iam-account=SERVICE_ACCOUNT  // google generate for you
$ gcloud iam service-accounts keys upload PUBLIC_KEY_FILE --iam-account=SERVICE_ACCOUNT          // upload your own
$ gcloud iam service-accounts keys list --iam-account=SERVICE_ACCOUNT                            // list keys for a given service account
$ gcloud iam service-accounts keys delete KEY_ID --iam-account=SERVICE_ACCOUNT                   // delete a key for a given service account  
```

2. Short-lived service account credentials
- limited lifetime of a few hours
- less risk than a service account keys

A user with `Service Account Token Creator Role` on a service account will be able to take advantage on short-lived credentials.
- They can generated an OAuth2 Access Token for the service account to access Google Cloud API as the service account
- Additionally a user of this role can use the `--impersonate-service-account` on any gcloud command to run it as a service account, without requiring keys.


1. First example
   
`roles/iam.serviceAccountUser` - Run operations as the service account.
   
```bash
The user does not have access to service account 'bucket-admin@service-accounts-demo-310307.iam.gserviceaccount.com'. User: 'johndoe@cloudnativecoach.com'. 
Ask a project owner to grant you the `iam.serviceAccountUser` role on the service account
```

Get the IAM policy for the project:

```bash
$ gcloud projects get-iam-policy service-accounts-demo-310307
```

Get the IAM policy for the given service account:

```bash
$ gcloud iam service-accounts get-iam-policy bucket-admin@service-accounts-demo-310307.iam.gserviceaccount.com
```

```bash
$ gcloud iam service-accounts add-iam-policy-binding bucket-admin@service-accounts-demo-310307.iam.gserviceaccount.com \
--member=user:johndoe@cloudnativecoach.com \
--role=roles/iam.serviceAccountUser

Updated IAM policy for serviceAccount [bucket-admin@service-accounts-demo-310307.iam.gserviceaccount.com].
bindings:
- members:
  - user:johndoe@cloudnativecoach.com
  role: roles/iam.serviceAccountUser
etag: BwW_ndIgGxY=
version: 1
```

We can also the the policy binding on the project level, but then it will apply to all service accounts in the project:

```bash
$ gcloud projects add-iam-policy-binding service-accounts-demo-310307 --member=user:johndoe@cloudnativecoach.com --role=roles/iam.serviceAccountUser
```

To remove policy binding use:

```bash
$ gcloud projects remove-iam-policy-binding service-accounts-demo-310307 --member=user:johndoe@cloudnativecoach.com --role=roles/iam.serviceAccountUser
```

2. Example

```bash
$ gcloud iam service-accounts add-iam-policy-binding sql-admin@service-accounts-demo-310307.iam.gserviceaccount.com \
--member=user:janedoe@cloudnativecoach.com \
--role=roles/iam.serviceAccountKeyAdmin
```

```bash
$ gcloud iam service-accounts get-iam-policy sql-admin@service-accounts-demo-310307.iam.gserviceaccount.com
```

Run it as `janedoe@cloudnativecoach.com`, here you don't have permission:

```bash
$ gcloud iam service-accounts keys create key.json --iam-account=bucket-admin@service-accounts-demo-310307.iam.gserviceaccount.com
ERROR: (gcloud.iam.service-accounts.keys.create) PERMISSION_DENIED: Permission iam.serviceAccountKeys.create is required to perform this operation on service account projects/-/serviceAccounts/bucket-admin@service-accounts-demo-310307.iam.gserviceaccount.com.
```

Run it as `janedoe@cloudnativecoach.com`, this should work:

```bash
$ gcloud iam service-accounts keys create key.json --iam-account=sql-admin@service-accounts-demo-310307.iam.gserviceaccount.com
created key [41e96e1438343a5d4698f965f281976d8e315b74] of type [json] as [key.json] for [sql-admin@service-accounts-demo-310307.iam.gserviceaccount.com]
$ gcloud iam service-accounts keys list --iam-account=sql-admin@service-accounts-demo-310307.iam.gserviceaccount.com

KEY_ID                                    CREATED_AT            EXPIRES_AT
41e96e1438343a5d4698f965f281976d8e315b74  2021-04-10T19:31:40Z  9999-12-31T23:59:59Z
58e542d28cc38045e262002af2cc6eace1752f97  2021-04-10T12:44:48Z  2023-04-11T19:22:42Z
```

```bash
$ gcloud sql instances create new-sql-instance --region=europe-west6
ERROR: (gcloud.sql.instances.create) User [janedoe@cloudnativecoach.com] does not have permission to access projects instance [service-accounts-demo-310307] (or it may not exist): The client is not authorized to make this request.
```

```bash
$ gcloud auth activate-service-account sql-admin@service-accounts-demo-310307.iam.gserviceaccount.com --key-file=key.json
Activated service account credentials for: [sql-admin@service-accounts-demo-310307.iam.gserviceaccount.com]
$ gcloud auth list
                        Credentialed Accounts
ACTIVE  ACCOUNT
        janedoe@cloudnativecoach.com
*       sql-admin@service-accounts-demo-310307.iam.gserviceaccount.com
```

```bash
$ gcloud sql instances create new-sql-instance --region=europe-west6
Creating Cloud SQL instance...done.
Created [https://sqladmin.googleapis.com/sql/v1beta4/projects/service-accounts-demo-310307/instances/new-sql-instance].
NAME              DATABASE_VERSION  LOCATION        TIER              PRIMARY_ADDRESS  PRIVATE_ADDRESS  STATUS
new-sql-instance  MYSQL_5_7         europe-west6-a  db-n1-standard-1  34.65.115.32     -                RUNNABLE
```

3. example

```bash
$ gcloud iam service-accounts add-iam-policy-binding redis-admin@service-accounts-demo-310307.iam.gserviceaccount.com \
--member=user:babydoe@cloudnativecoach.com \
--role=roles/iam.serviceAccountTokenCreator
```

```bash
$ gcloud iam service-accounts get-iam-policy redis-admin@service-accounts-demo-310307.iam.gserviceaccount.com 
```

Run as `babyjoe@cloudnativecoach.com`

```bash
$ gcloud redis instances create new-redis-instance --region=europe-west6
ERROR: (gcloud.redis.instances.create) PERMISSION_DENIED: Permission 'redis.instances.create' denied on 'projects/service-accounts-demo-310307/locations/europe-west6/instances/new-redis-instance'
```

```bash
$ gcloud redis instances create new-redis-instance --region=europe-west6 --impersonate-service-account=redis-admin@service-accounts-demo-310307.iam.gserviceaccount.com
```










Resources:

* https://www.youtube.com/playlist?list=PLIivdWyY5sqIlPnZ7cvkg2Ck-8ZZ8TA5t


