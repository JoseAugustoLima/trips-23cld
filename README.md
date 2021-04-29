## AWS SAM Application for Managing Trips

This is a sample application to demonstrate how to build an application on AWS Serverless Envinronment using the AWS SAM, Amazon API Gateway, AWS Lambda and Amazon DynamoDB. It also uses the DynamoDBMapper ORM structure to map Trip records in a DynamoDB table to a RESTful API for managing Trips.


## Requirements

* AWS CLI already configured with at least PowerUser permission
* [Java SE Development Kit 8 installed](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Docker installed](https://www.docker.com/community-edition)
* [Maven](https://maven.apache.org/install.html)
* [SAM CLI](https://github.com/awslabs/aws-sam-cli)
* [Python 3](https://docs.python.org/3/)


## Setup process

### Installing dependencies

We use `maven` to install our dependencies and package our application into a JAR file:

```bash
mvn install
```

### Local development

**Invoking function locally through local API Gateway**

1. Start DynamoDB Local in a Docker container. `docker run -p 8000:8000 -v $(pwd)/local/dynamodb:/data/ amazon/dynamodb-local -jar DynamoDBLocal.jar -sharedDb -dbPath /data`

2. Create the DynamoDB table. `aws dynamodb create-table --table-name trips --attribute-definitions AttributeName=country,AttributeType=S AttributeName=city,AttributeType=S --key-schema AttributeName=country,KeyType=HASH AttributeName=city,KeyType=RANGE --billing-mode PAY_PER_REQUEST --endpoint-url http://localhost:8000`
 
 - (1) For Linux environment you need to change the owner of the "local" directory from root to the logged user before running the command above.

 - (2) If the table already exist, you can delete: `aws dynamodb delete-table --table-name trips --endpoint-url http://localhost:8000`

3. Start the SAM local API.
 - On a Mac: `sam local start-api --env-vars src/test/resources/test_environment_mac.json --skip-pull-image --warm-containers eager`
 - On Windows: `sam local start-api --env-vars src/test/resources/test_environment_windows.json --skip-pull-image --warm-containers eager`
 - On Linux: `sam local start-api --env-vars src/test/resources/test_environment_linux.json --skip-pull-image --warm-containers eager`
 
 OBS:  
 
 - (1) If you already have the container locally (in your case the java8), then you can use --skip-pull-image to remove the download
 - (2) --warm-containers eager: Specifies how AWS SAM CLI manages containers for each function. Two modes are available: 
       EAGER: Containers for all functions are loaded at startup and persist between invocations.
       LAZY:  Containers are only loaded when each function is first invoked. Those containers persist for additional invocations.
 - (3) For Linux environments you may need to update the URLs in the file "src/test/resources/test_environment_linux.json" replacing the loopback IP (127.0.0.1) to your IP before running the command above.

Now to make requests to the endpoints you should use the postman_collection.json file located on path src/test/resources/Trips to import an API Rest Collection into Postman.

**SAM CLI** is used to emulate both Lambda and API Gateway locally and uses our `template.yaml` to understand how to bootstrap this environment (runtime, where the source code is, etc.)


### Packaging and deployment

**Deploying function to a CloudFormation stack on Amazon Web Services**

AWS Lambda Java runtime accepts either a zip file or a standalone JAR file - We use the latter in this example. SAM will use `CodeUri` property to know where to look up for both application and dependencies:

Firstly, we need a `S3 bucket` where we can upload our Lambda functions packaged as ZIP before we deploy anything - If you don't have a S3 bucket to store code artifacts then this is a good time to create one:

```bash
export BUCKET_NAME=my-cool-new-bucket
aws s3 mb s3://$BUCKET_NAME
```

Next, run the following command to package our Lambda function to the S3 bucket:

```bash
sam package \
    --template-file template.yaml \
    --output-template-file packaged.yaml \
    --s3-bucket $BUCKET_NAME
```

Next, the following command will create a Cloudformation Stack and deploy your SAM resources.

```bash
sam deploy \
    --template-file packaged.yaml \
    --stack-name trips \
    --capabilities CAPABILITY_IAM
```

> **See [Serverless Application Model (SAM) HOWTO Guide](https://github.com/awslabs/serverless-application-model/blob/master/HOWTO.md) for more details in how to get started.**

After deployment is complete you can run the following command to retrieve the API Gateway Endpoint URL:

```bash
aws cloudformation describe-stacks \
    --stack-name trips \
    --query 'Stacks[].Outputs'
```
