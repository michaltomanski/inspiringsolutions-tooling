# inspiringsolutions-tooling

[![Join the chat at https://gitter.im/MeStudent/inspiringsolutions-tooling](https://badges.gitter.im/MeStudent/inspiringsolutions-tooling.svg)](https://gitter.im/MeStudent/inspiringsolutions-tooling?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build status at https://travis-ci.org/MeStudent/inspiringsolutions-tooling](https://travis-ci.org/MeStudent/inspiringsolutions-tooling.svg)](https://travis-ci.org/MeStudent/inspiringsolutions-tooling)
[![Codacy Badge](https://api.codacy.com/project/badge/coverage/834da071b5204b25ad78265e9cd150fb)](https://www.codacy.com/app/pdolega/inspiringsolutions-tooling)
[![Heroku](https://heroku-badge.herokuapp.com/?app=peaceful-ridge-14628)](https://peaceful-ridge-14628.herokuapp.com/)

## What is this

Sample project used for workshops during Inspiring Solutions conference

The websocket location is ws://peaceful-ridge-14628.herokuapp.com/channel/{keyword}

## How to build

- make sure you have recent Java 8 installed (Oracle or OpenJDK)
- make sure you have `node` in version >= `4.2.x`
    - On unices you could try:
        - `rm -rf ~/.nvm`
        - `curl -L https://raw.githubusercontent.com/creationix/nvm/master/install.sh | sh`
        - `source ~/.nvm/nvm.sh`
        - `nvm install 4.2.2`
        - `nvm use 4.2.2`
    - On Win try maybe: https://github.com/coreybutler/nvm-windows
- run `npm install`
- run `./activator compile run`
- go to `localhost:9000`
