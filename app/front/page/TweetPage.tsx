import * as React from "react";
import {TweetSocketConnection, Tweet} from "../socket/TweetSocketConnection";
import {List} from "immutable";
import ReactLink = __React.ReactLink;

export class State {
    tweets: List<Tweet> = List<Tweet>();
    filter: string = "";
}

export class TweetPage extends React.Component<void, State> {
    private socket: TweetSocketConnection;
    
    constructor() {
        super();
        this.handleTweet = this.handleTweet.bind(this);
        this.filterClicked = this.filterClicked.bind(this);
        this.linkState = this.linkState.bind(this);
    }

    componentWillMount() {
        this.setState(new State());
        this.socket = new TweetSocketConnection(this.handleTweet.bind(this));
        this.socket.connect();
    }
    
    componentWillUnmount() {
        this.socket.disconnect();
        this.socket = null;
    }

    render(): React.ReactElement<any> {
        return (
            <div>
                <label htmlFor="filter">Filter tweets</label>
                <input id="filter" type="text" valueLink={this.linkState("filter")}/>
                <button onClick={this.filterClicked}>Filter!</button>
                <hr/>
                <br/>
                {this.renderList()}
            </div>
        );
    }

    renderList(): React.ReactElement<any> {
        if(this.state.tweets.isEmpty()) {
            return <span>No tweets (yet)!</span>
        } else {
            return (
                <div>
                    {this.renderTweets()}
                </div>
            );
        }
    }

    private renderTweets(): React.ReactElement<any>[] {
        return this.state.tweets.map(tweet => {
            return (
                <div key={tweet.id} className="tweet">
                    <img src={tweet.userImg}/>

                    <span>Author:</span>
                    <span>{tweet.user}</span>

                    <span>{tweet.text}</span>
                </div>
            );
        }).toArray();
    }

    private linkState(varName: string) {
        return new LinkState(this.state, varName, state => this.setState(state));
    }

    private filterClicked() {
        this.socket.disconnect();

        let filter = this.state.filter.trim();
        this.socket.connect(filter != null ? filter : null)
    }

    private handleTweet(tweet: Tweet) {
        console.info(`Tweet received [ID: ${tweet.id}]: ${tweet.text}`);
        let state = this.state;
        let newList = state.tweets.unshift(tweet);
        state.tweets = newList.size > 20 ? newList.setSize(20) : newList;
        this.setState(state);
    }
}

class LinkState implements ReactLink<string> {
    value: string = null;

    constructor(private state: any, private varName: string, private updateCallback: (state: any) => void) {
        this.value = state[varName];
    }

    requestChange(newValue: string):void {
        this.state[this.varName] = newValue;
        this.updateCallback(this.state);
    }
}