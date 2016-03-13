import * as React from "react";

export class State {
}

export class TweetPage extends React.Component<void, State> {
    constructor() {
        super();
    }

    componentWillMount() {
        this.setState(new State());
    }

    render() {
        return (
            <span>Test React!</span>
        );
    }
}