
export class Tweet {
    id: string;
    text:string;
    user:string;
    userImg:string;
}

export class TweetSocketConnection {

    private socket: WebSocket;

    constructor(
        private onTweet: (tweet: Tweet) => void
    ) {
        this.tweetPushed = this.tweetPushed.bind(this);
    }

    connect(filter?: string) {
        let protocol = (window.location.protocol == "http:") ? "ws" : "wss";
        let host = window.location.host;
        let webSocketUrl = `${protocol}://${host}/channel` + (filter != null ? `?filter=${filter}` : "");
        console.debug(`Actual websocket address: ${webSocketUrl}`);

        this.socket = new WebSocket(webSocketUrl);
        this.socket.onmessage = this.tweetPushed;
    }

    disconnect() {
        console.info("Web Socket is to be closed...");
        this.socket.close();
        this.socket = null;
    }

    private tweetPushed(msg: MessageEvent) {
        let tweet = JSON.parse(msg.data);
        this.onTweet(tweet);
    }
}