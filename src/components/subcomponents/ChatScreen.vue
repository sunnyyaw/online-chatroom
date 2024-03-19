<template>
  <div ref="container" class="container">
    <p v-for="message,index in messages" 
    :class="
    message.self ? 'chatscreen-text' : 'chatscreen-text align-right'" >
      {{ message.pureMessage }}
    </p>
  </div>
</template>
<script>
export default {
  data() {
    return {
      messages: [],
    }
  },
  methods: {
    init() {
      const socket = this.$store.state.socket;
      socket.addEventListener('message',(event) => {
        const message = event.data;
        if (message.startsWith('M ')) {
          const pureMessage = message.substring(2);
          const username = pureMessage.slice(0,pureMessage.indexOf(':'));
          const selfname = localStorage.getItem('username');
          this.messages.push({
            self: username === selfname,
            pureMessage
          });
        }
      });
    }
  }
}
</script>
<style scoped>
.align-right {
  text-align: right;
}
.chatscreen-text {
  margin: 0.2rem 0;
}
.container {
  padding: 5px;
  display: flex;
  flex-direction: column;
  overflow: auto;
}
</style>