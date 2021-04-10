`User Account`
- Has email/password
- User accounts should never be shared
- has IAM Roles, which gives him permissions to do stuff
- can be `enabled` or `disabled`

`Service Account` 
- An identity that a compute-instance or an application can use to run API requests on your behalf.
- Users can also impersonate service accounts, allowing multiple users to access everything that a service account can access.  
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
$ gcloud iam service-account create NAME 
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

It is also important who has access to the service accounts.
When you look into a project's IAM policy take note on the accounts which have roles associated with managing or using service accounts
- `Service Account Token Creator`
- `Service Account Admin`
- `Service Account Key Admin`

In addition to the project's IAM policy there is an IAM policy for each service account in your project.







Resources:

* https://www.youtube.com/playlist?list=PLIivdWyY5sqIlPnZ7cvkg2Ck-8ZZ8TA5t


