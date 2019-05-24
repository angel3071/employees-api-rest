<!-- PROJECT SHIELDS -->
[![Build Status][build-shield]]()
[![Contributors][contributors-shield]]()



<!-- PROJECT LOGO -->
<br />
<p align="center">

  <h3 align="center">Serverless Employees Api</h3>

  <p align="center">
    A serverless approach to an Employees api!
    <br />
    <a href="https://documenter.getpostman.com/view/7170007/S1TR4zZz"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/angel3071/employees-api-rest/issues">Report Bug</a>
    ·
    <a href="https://github.com/angel3071/employees-api-rest/issues">Request Feature</a>
  </p>
</p>



<!-- TABLE OF CONTENTS -->
## Table of Contents

* [About the Project](#about-the-project)
  * [Built With](#built-with)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
* [Usage](#usage)
* [Contributing](#contributing)
* [License](#license)
* [Contact](#contact)
* [Acknowledgements](#acknowledgements)



<!-- ABOUT THE PROJECT -->
## About The Project

![Architecture diagram](images/AWS-Lambda-and-DynamoDB-Architecture.png)

There are always several different ways to solve a problem in this situation I
see the oportunity and I take the risk, I hope you to like it.

The proposed solution to the requiriement was to develop a serverless
implementation of the Employees api, all the specification on the request was
accomplished and some other cool stuff was involved on the process.

Some reasons to choose a serverless approach:
* Pricing!
* Zero system administration
* Scalable!

And with the Infraestructure as Code approach everything its super easy to move
around, deploy and take care of the services configurationd and maintanence on
the git respository itself.


### Built With
On a global point of view:
* [API Gateway](https://aws.amazon.com/es/api-gateway/)
* [AWS Lambda](https://aws.amazon.com/es/lambda/)
* [DynamoDB](https://aws.amazon.com/es/dynamodb/)
* [AWS CloudFormation](https://aws.amazon.com/es/cloudformation/)

On the Code point of view:
* [Java8]
* [Gradle]
* [aws sdk]
* [Jackson]


<!-- GETTING STARTED -->
## Getting Started

This api its ready for you to deploy it, you just need an aws account (obviously)
To get your own copy up and running follow these simple example steps.

### Prerequisites

This is an example of how to list things you need to use the software and how to install them.
#### Tools
- gradle
- aws cli

#### S3 Bucket for code artifacts
```sh
aws s3 mb s3://{whetever-you-want-to-call-your-bucket}
```

### Deployment

1. Clone the repo
```sh
git clone https://github.com/angel3071/employees-api-rest
```
2. Build
```sh
gradle build
```
4. Package
```sh
aws cloudformation package --template-file samTemplate.yaml --s3-bucket {whetever-your-bucket-its-named} --output-template-file outputSamTemplate.yaml
```
5. Deploy
```sh
aws cloudformation deploy --stack-name EmployeesApi --template outputSamTemplate.yaml --capabilities CAPABILITY_IAM
```

On the deployment a custom resource its created and it loads some example data
to de databse to be ready to testing.



<!-- USAGE EXAMPLES -->
## Usage (the api its actually already live!)

All the possible methods are described on the documentation, please refer to
them, a little example:

```sh
curl --location --request GET "https://1suc3jfv04.execute-api.us-east-1.amazonaws.com/Prod/employees/56b6c351-2c5c-4f5f-930d-6ad02b0d8c80"

{
    "id": "56b6c351-2c5c-4f5f-930d-6ad02b0d8c80",
    "firstName": "Jhon",
    "middleInitial": "S",
    "lastName": "Snow",
    "dateOfBirth": "01/01/1991",
    "dateOfEmployment": "01/01/2019",
    "status": "ACTIVE"
}
```

_For more examples, please refer to the [Documentation](https://example.com)_



<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.



<!-- CONTACT -->
## Contact

Your Name - [@your_twitter](https://twitter.com/your_username) - email@example.com

Project Link: [https://github.com/your_username/repo_name](https://github.com/your_username/repo_name)



<!-- ACKNOWLEDGEMENTS -->
## Acknowledgements
* [GitHub Emoji Cheat Sheet](https://www.webpagefx.com/tools/emoji-cheat-sheet)
* [Img Shields](https://shields.io)
* [Choose an Open Source License](https://choosealicense.com)
* [GitHub Pages](https://pages.github.com)
* [Animate.css](https://daneden.github.io/animate.css)
* [Loaders.css](https://connoratherton.com/loaders)
* [Slick Carousel](https://kenwheeler.github.io/slick)
* [Smooth Scroll](https://github.com/cferdinandi/smooth-scroll)
* [Sticky Kit](http://leafo.net/sticky-kit)
* [JVectorMap](http://jvectormap.com)
* [Font Awesome](https://fontawesome.com)





<!-- MARKDOWN LINKS & IMAGES -->
[build-shield]: https://img.shields.io/badge/build-passing-brightgreen.svg?style=flat-square
[contributors-shield]: https://img.shields.io/badge/contributors-1-orange.svg?style=flat-square
[license-shield]: https://img.shields.io/badge/license-MIT-blue.svg?style=flat-square
[license-url]: https://choosealicense.com/licenses/mit
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=flat-square&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/othneildrew
[product-screenshot]: https://raw.githubusercontent.com/othneildrew/Best-README-Template/master/screenshot.png
