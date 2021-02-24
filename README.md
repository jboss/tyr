# Tyr 

![Powered by OpenShift Online](https://www.openshift.com/images/logos/powered_by_openshift.png)

## Description 

Tyr is a Pull request status check tool for maintaining a clean and uniform PR format of your project based on a preset template.\
Check it out in action: https://www.youtube.com/watch?v=qZRcMQ6qIpg&t=6s&ab

## Development

1. Build Tyr with Maven, using command `mvn clean install`
1. Create a testing repository - for start you can fork 
https://github.com/xstefank/test-repo where the Pull request template is already set up, or 
you can create your own.
    > - For creating new repository check the github manuals/help.
    > - Pull requests needs to be made to **your** repository, therefore when you'll be doing Pull Request you need to specify this 
    under the base fork field in the "Open a Pull Request" page.
1. The project requires two [MicroProfile Config](https://github.com/eclipse/microprofile-config/blob/master/spec/src/main/asciidoc/microprofile-config-spec.asciidoc) properties to be set up to run correctly.  
    1. `tyr.github.oauth.token` - Represents your unique GitHub OAuth token provided to Tyr.  
        1. Go to your profile -> settings (top right) -> Developer Settings -> Personal access tokens  
        1. Generate new token
        1. Tick repo:status
        1. Generate token
        1. Use it as a value of this property, so the Tyr will be able to communicate with GitHub.
   2. Next one is `tyr.template.format.file`, alternatively `tyr.template.format.url` - Represents the path to configuration file specifying desired format of the Pull request. Example can be found in `tyr-core/src/main/resources/format-example.yaml`
    > Both properties can be passed to Tyr as:\
        a. application properties - set once in `tyr-core/src/main/resources/application.properties`. Necessary to rebuild Tyr after every adjustment of the file.\
        b. system properties - passed as a parameter when starting Tyr. e.g. `-Dtyr.template.format.file=/path/to/file` (preferred option)\
        c. as environment property TYR_TEMPLATE_FORMAT_FILE=/path/to/file
1. start Tyr - `java -jar tyr-webhook-runner.jar` with above MP config properties set.\
   (as for instance with system properties, e.g. `java -Dtyr.template.format.file="/path/to/file" -Dtyr.github.oauth.token="afegxh64hnxh4646..." -jar tyr-webhook-runner.jar`)
1. Expose local server instance with ngrok - https://ngrok.com/
    > Here you need to expose **http** port **8080** as the default application
    interface where the application runs. Consult the ngrok documentation for the
    information of how to specify these arguments.
1. Add a webhook for Tyr
    1. In you test-repo - go to Settings -> Webhooks -> Add webhook
    1. Fill in Payload URL -> *your local exposed IP which is provided by 
    ngrok + **“/pull-request”*** (e.g., http://f202cf7c.ngrok.io/pull-request)
    1. Fill in Content type -> application/json
    1. Set *Let me select individual events* and check only *Pull requests* 
    and *Pull request review comments*
    1. Leave Active set and click “Add webhook”
    1 Don’t worry about the error - the first JSON differs from what the 
    subsequent PR JSONs will look like.
1. Last thing is to edit your format template file.  
    1. Open `format-template.yaml` which path you've set in a 3rd step.
    1. Edit value of the variable `repository` so it points to __your__ test-repo.
    1. Edit value of the variable `statusUrl` so it points to __your__ test-repo or leave it default.
    1. Now you have to set if you want Tyr to use any CI. 
       * If NO, remove variable `CI` and all of it's content.
       * If YES, set CI name, and it's properties. We are using TeamCity CI which properties are defined in the `TeamCityProperties` interface. Also set MP property `tyr.whitelist.enabled=true` same way as it is described in step 3.
1. Now you should have everything prepared
1. If you create a PR now in your test-repo (create a new branch based on master for instance) 
you should already see some output in the server logs and everything should be working now.
    > Be careful to which repository and branch you are doing the PR!!! 
    You need to specify **your** repository, the point is to try to set up Tyr 
    on **your** repository.
1. If everything runs ok the PR should be updated with the valid error or 
green color, and in terminal where the server runs you’ll see
**Status update: 201**. 
   > If not, check Tyr output for any Exceptions. Also check incoming ngrok HTTP requests to determine error code. Try to rebuild and rerun Tyr. Check GitHub if token was used by Tyr. Check latest info at the bottom of your test-repo webhook settings.

## OpenShift deployment

This project contains an openshift profile to be easily deployable to
[Openshift platform](https://www.openshift.com/). 

To deploy to Openshift:

1. login to your OS account (`oc login`)
1. `mvn fabric8:deploy -Popenshift`
