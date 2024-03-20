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
        socket.addEventListener("open",(event) => {
          store.commit('load',socket);
          this.$refs.chatScreen.init();
          this.$refs.friends.init();
        });
        socket.addEventListener("message",(event) => {
          console.log("Message from server: ",event.data);
        });
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
  height: 5%;
}
.text-pad {
  flex-grow: 1;
}
</style>