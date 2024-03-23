<template>
  <div class="container">
    <aside class="friend-box">
      <Friends ref="friends"/>
    </aside>
    <main class="main-sector">
      <ChatScreen class="chat-screen" ref="chatScreen"/>
      <ToolBox class="tool-box"/>
      <TextPad class="text-pad"/>
    </main>
  </div>
</template>
<script>
import Friends from './subcomponents/Friends.vue';
import ChatScreen from './subcomponents/ChatScreen.vue';
import ToolBox from './subcomponents/ToolBox.vue';
import TextPad from './subcomponents/TextPad.vue';
  export default {
    components: {
      Friends,ChatScreen,ToolBox,TextPad,
    },
    data () {
      return {
        reconnectTimes: 0,
        reconnectLimit: Number.MAX_VALUE,
      }
    },
    mounted() {
      this.initWebSocket();
    },
    methods: {
      initWebSocket() {
        const store = this.$store;
        const token = localStorage.getItem('token');
        const socket = new WebSocket(`ws://localhost:8080/websocket/${token}`);
        store.commit('load',socket);
        socket.addEventListener("open",(event) => {
          this.$refs.chatScreen.init();
          this.$refs.friends.init();
          this.heartBeat(6000);
        });
        socket.addEventListener('error',(event) => {
          this.$message.error('无法连接至服务器!');
          this.reconnect(6000);
        });
        socket.addEventListener('close',(event) => {
          console.log('服务器断开连接');
        });
        socket.addEventListener("message",(event) => {
          console.log("Message from server: ",event.data);
        });
      },
      reconnect(time) {
        setTimeout(() => {
          if (!this.$store.state.socket) {
            return;
          }
          if (this.reconnectTimes < this.reconnectLimit) {
            this.initWebSocket();
            this.reconnectTimes++;
            console.log(`第${this.reconnectTimes}次尝试重连`);
          }
        }, time);
      },
      heartBeat(time) {
        setTimeout(() => {
          if (!this.$store.state.socket) {
            return;
          }
          const readyState = this.$store.state.socket.readyState;
          if (readyState === WebSocket.OPEN ) {
            this.$store.state.socket.send('ping');
            this.heartBeat(time);
            this.reconnectTimes = 0;
          } else {
            this.reconnect(time);
          }
        },time);
      }
    }
  }
</script>
<style scoped>
.container {
  display: flex;
  height: 100%;
}
.friend-box {
  height: 100%;
  width: 20%;
  border-inline-end: 1px solid rgba(5,5,5,0.06);
}
.main-sector {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}
.chat-screen {
  height: 70%;
}
.tool-box {
  height: 1.5rem;
}
.text-pad {
  flex-grow: 1;
}
</style>