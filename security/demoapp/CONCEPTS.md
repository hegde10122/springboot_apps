### Why spring security

Application security framework
-------------------------------

Username/password authentication
SSO/OKTA/LDAP
App level authorization
Intra app authorization like OAuth
Microservice security (using tokens,JWT)
Method level security

### 5 core concepts in spring security

Authentication
Authorization
Principal
Granted Authority
Roles

## Authentication
Answering the question --- Who are you ? --- authentication
Password --- this is a type of knowledge based authentication
phone/text messages, keycards,badges --- possession based authentication

2FA -- knowledge + possession

## Authorization
Answering the question ---- Cna this user do this? 

## Principal
Currently logged in user. A person whom we have identified using authentication.

## Granted Authority
A logged in user is allowed to do certain activities --- list of activities permitted for any authenticated user.They are fine-grained.

## Roles
They are coarse-grained

## What do we do normally for authentication ?
1) We use the AuthenticationManagerBuilder
2) Set the configuration on this builder



