/// <reference path="../../typings/main.d.ts"/>

import * as React from "react";
import * as ReactDOM from "react-dom";
import {TweetPage} from "./page/TweetPage";

window["renderReact"] = function() {
    console.debug(`Sample page initialization`);
    ReactDOM.render(
        <TweetPage/>,
        document.getElementById('react')
    );
}