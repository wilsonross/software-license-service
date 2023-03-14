## About

<div>
  <br />
  <p>Uses AES encryption to issue and validate software licenses as a RESTful service without needing a database. It contains two endpoints that POST requests will be sent to <code>/api/license/create</code> and <code>/api/license/validate</code>. Creating a license requires bearer token authentication, provided by a known secret as an environment variable.</p>
  <br />
</div>

## Getting Started

<div>
  <br />
  <p>The following commands demonstrate the environment variables and the environment variables required to run the service. The <code>PASSWORD</code> variable is a secret used to create a secret key for encryption. <code>SALT</code> is random data used as additional input for the hashing function. <code>TOKEN</code> is a secret key used as bearer authentication when attempting to create a license.</p>
  <br />
</div>

Run server:
```bash
PASSWORD=password SALT=salt TOKEN=token ./gradlew clean build bootRun
```

Run tests:
```bash
./gradlew clean build :test --tests "com.wilsonross.license.LicenseApplicationTests"
```

<br />

## Creating Requests

<div>
  <br />
  <p>Examples are shown below using cURL to issue a request to each endpoint. The bearer token is the <code>TOKEN</code> environment variable encoded to base 64.</p>
  <br />
</div>

Create license:
```bash
curl --header "Content-Type: application/json" \
	-H "Authorization: Bearer <base64_encoded_token>" \
	--request POST \
	--data '{"firstName":"<first_name>","lastName":"<last_name>","softwarePackage":"<software_package>"}' \
	localhost:8080/api/license/create
```

Validate license:
```bash
curl --header "Content-Type: application/json" \
	--request POST \
	--data '{"firstName":"<first_name>","lastName":"<last_name>","licenseKey":"<license_key>"}' \ 
	localhost:8080/api/license/validate
```

<br />

## Contact

<br />

Ross Wilson - [ross.wilson.190298@gmail.com](mailto:ross.wilson.190298@gmail.com)

<br />
