# tyr
  
Test CI change

![Powered by OpenShift Online](https://www.openshift.com/images/logos/powered_by_openshift.png)

[![Build Status](https://travis-ci.org/xstefank/tyr.svg?branch=master)](https://travis-ci.org/xstefank/tyr)

Pull request template status check

## Running

1. Clone Tyr - https://github.com/xstefank/tyr
1. Build with Maven - mvn clean install
1. Create a testing repository - for start you can fork my https://github.com/xstefank/test-repo where the PR template is set up or you can create your own :)
    - For creating new repository check the github manuals/help
    - PRs needs to be created to YOUR repository, you need to specify it under the base fork field in the Open a Pull Request page
1. The project requires two properties to be set up to run correctly
    - GitHub OAuth token - the setup will be described later
    - Configuration file specifying the format of the PR - the example can be found in src/main/resources/format.yaml, there are two ways how to specify to the server where is this file located
        - System property **template.format.file** (https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html) - this is a standard way how to pass some information to the running server instance
            - You can set by adding **-Dtemplate.format.file=/path/to/your/config/file** when you start the server
        - Second option is to paste the **format.yaml** (you can just copy the example) into **$JBOSS_HOME/standalone/configuration/** folder ($JBOSS_HOME is the directory where is your server located)
1. Start WildFly / EAP in the standalone mode running on localhost
    - **$JBOSS_HOME/bin/standalone.sh (-Dtemplate.format.file=/path/to/your/config/file)**
1. Deploy tyr.war to the server instance
1. Expose local server instance with ngrok - https://ngrok.com/
    - Here you need to expose **http** port **8080** as the default application interface where the WFLY/JBOSS applications run, consult the ngrok documentation for the information of how to specify these arguments
1. Now you should have everything prepared locally, next step is to let GitHub know where your Tyr instance runs
1. Add a webhook for Tyr
    - In you test-repo - go to Settings -> Webhooks -> Add webhook
    - Fill in Payload URL -> *your local exposed IP which is provided by ngrok + “/tyr/pull-request”* (e. g., http://f202cf7c.ngrok.io/tyr/pull-request)
    - Fill in Content type -> application/json
    - Set *Let me select individual events* and check only *Pull requests* and *Pull request review comments*
    - Leave Active set and click “Add webhook”
    - Don’t worry about the error - the first JSON differs from what the subsequent PR JSONs will look like
1. If you create a PR now (create a new branch based on master for instance) you should already see some output in the server logs
    - Be careful to which repository and branch you are doing the PR!!! You need to specify your repository, the point is to try to set up Tyr on your repository
1. However, you will still probably see problems with the custom PR check which the app is trying to send to GitHub as there is a requirement to provide a oauth token to the GH API
1. Go to your profile -> settings (top right) -> Developer Settings -> Personal access tokens
1. Generate new token
1. Tick repo:status
1. Generate token
1. The last step is to let Tyr know what is the token value
    - add the token to **config.properties** file (example is provided in the src/main/resources folder) and copy this file to the WFLY/JBOSS configuration directory - **$JBOSS_HOME/standalone/configuration**
1. Restart the server
1. Rerun (update existing or create new PR) - everything should be working now
1. If everything runs ok the PR should be updated with the valid error or green color, and in terminal where the server runs you’ll see **Status update: 201**

