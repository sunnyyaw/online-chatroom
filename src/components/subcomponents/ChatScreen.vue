<template>
  <div ref="container" class="container">
    <div class="chatscreen-chater">{{ $store.state.chater }}</div>
    <div class="chatscreen-messages">
      <p v-for="message,index in messages[$store.state.chater]?.messages" 
      :class="
      message.username === $store.state.username ? 'chatscreen-text align-right' : 'chatscreen-text'" >
        {{ message.username + ': ' + message.pureMessage + ' ' + message.dateTime}}
      </p>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      messages: {},
    }
  },
  methods: {
    init() {
      const socket = this.$store.state.socket;
      this.messages = this.$store.state.messages;
      socket.addEventListener('message',this.handleMessage);
    },
    handleMessage (event) {
      const message = event.data;
      if (message.startsWith('M/')) {
        const head = message.slice(0,message.indexOf('#'));
        const parts = head.split('/');
        const channel = '公共聊天室';
        const username = parts[1];
        const dateTime = parts[2];
        const pureMessage = message.slice(message.indexOf('#') + 1);
        const msg = {
          username,
          pureMessage,
          dateTime
        };
        this.$store.commit('pushMessage',{
          msg,
          channel
        });
      } else if (message.startsWith('P/')) {
        const head = message.slice(0,message.indexOf('#'));
        const parts = head.split('/');
        const channel = parts[1];
        const username = parts[2];
        const dateTime = parts[3];
        const pureMessage = message.slice(message.indexOf('#') + 1);
        const msg = {
          username,
          pureMessage,
          dateTime
        };
        this.$store.commit('pushMessage',{
          msg,
          channel
        });
      }
      this.$store.commit('storeMessage');
    }
  }
}
</script>
<style scoped>
.align-right {
  text-align: right;
}
.chatscreen-messages {
  padding: 5px;
}
.chatscreen-chater {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 2rem;
}
.chatscreen-text {
  margin: 0.2rem 0;
}
.container {
  display: flex;
  flex-direction: column;
  overflow: auto;
}
</style>