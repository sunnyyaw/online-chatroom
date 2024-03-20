<template>
  <div ref="container" class="container">
    <div class="chatscreen-chater">{{ $store.state.chater }}</div>
    <div class="chatscreen-messages">
      <p v-for="message,index in messages[$store.state.chater]" 
      :class="
      message.self ? 'chatscreen-text align-right' : 'chatscreen-text'" >
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
      const selfname = localStorage.getItem('username');
      const messageStr = localStorage.getItem(`${selfname}message`);
      if (messageStr) {
        this.messages = JSON.parse(messageStr);
      }
      socket.addEventListener('message',this.handleMessage);
    },
    handleMessage (event) {
      const message = event.data;
      const selfname = localStorage.getItem('username');
      if (message.startsWith('M/')) {
        const head = message.slice(0,message.indexOf('#'));
        const parts = head.split('/');
        const username = parts[1];
        const dateTime = parts[2];
        const pureMessage = message.slice(message.indexOf('#') + 1);
        const msg = {
          self: username === selfname,
          username,
          pureMessage,
          dateTime
        };
        if (!this.messages['公共聊天室']) {
          this.messages['公共聊天室'] = [msg];
        } else {
          this.messages['公共聊天室'].push(msg);
        }
      } else if (message.startsWith('P/')) {
        const head = message.slice(0,message.indexOf('#'));
        const parts = head.split('/');
        const from = parts[1];
        const username = parts[2];
        const dateTime = parts[3];
        const pureMessage = message.slice(message.indexOf('#') + 1);
        const msg = {
          self: username === selfname,
          username,
          pureMessage,
          dateTime
        };
        if (!this.messages[from]) {
          this.messages[from] = [msg];
        } else {
          this.messages[from].push(msg);
        }
      }
      localStorage.setItem(`${selfname}message`,JSON.stringify(this.messages));
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