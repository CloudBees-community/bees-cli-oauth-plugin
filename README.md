What is this?
=============

This plugin adds several `oauth:*` commands to [CloudBees SDK](http://wiki.cloudbees.com/bin/view/RUN/BeesSDK) command line tool to interact with the OAuth service. These commands are still in the early stage and is subject to change.

To install this plugin, run the following command from your command line:

    $ bees plugin:install org.cloudbees.sdk.plugins:oauth-plugin

Run `bees help` to see the list of availble `oauth:*` commands

##Register Protected App 

Protected app is an app that wants to register it's own scope. Such apps, when receive an OAuth token, verifies the token and check for it's scope and also check for other authorizations.

    $ bees oauth:app:register --account CLOUDBEES_ACCOUNT_NAME --callback https://myprotectedapp.example.com/callback --grant-type client_credentials --url https://myprotectedapp.example.com -n "My Protected App” -S https://myprotectedapp.example.com/read="Allow to read your data" -S https://myprotectedapp.example.com/write="Allow to update your data"

## Register client App

Client apps are the one who generate a token with desired scope and calls a protected app.

    $ bees oauth:app:register --account CLOUDBEES_ACCOUNT_NAME --callback https://myprotectedapp.example.com/callback --grant-type client_credentials --url https://myprotectedapp.example.com -n "My Protected App” 
    
## Create access token

    $ bees oauth:token:create --account CLOUDBEES_ACCOUNT_NAME -clientId xxxxx -clientSecret xxxxx -scope https://myprotectedapp.example.com/read
    
## Create refresh token

    $ bees oauth:token:create --account CLOUDBEES_ACCOUNT_NAME -clientId xxxxx -clientSecret xxxxx -scope https://myprotectedapp.example.com/read -generate-refresh-token
