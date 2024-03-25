<template>
  <div ref="container" class="container">
    <div class="chatscreen-chater">{{ $store.state.chater }}</div>
    <div ref="messageBox" class="chatscreen-messages">
      <div v-for="message,index in messages[$store.state.chater]?.messages" 
      :class="message.username === $store.state.username ? 
      'chatscreen-text align-right' : 'chatscreen-text'" >
      <div class="chatscreen-username-datetime">
        <span class="chatscreen-datetime">{{message.dateTime}}</span>
      </div>
      <div class="chatscreen-username-datetime">
        <span class="chatscreen-username">{{ message.username }}</span>
        <div class="chatscreen-arrow"></div>
        <div class="chatscreen-message">{{message.pureMessage}}</div>
      </div>
      </div>
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
  watch: {
    '$store.state.chater': function() {
      this.$nextTick(() => {
        this.$refs.messageBox.lastElementChild?.scrollIntoView();
      });
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
        if (this.$store.state.chater === channel) {
          this.$nextTick(() => {
            this.$refs.messageBox.lastElementChild?.scrollIntoView();
          });
        }
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
        if (this.$store.state.chater === channel) {
          this.$nextTick(() => {
            this.$refs.messageBox.lastElementChild?.scrollIntoView();
          });
        }
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
.align-right .chatscreen-username-datetime {
  flex-direction: row-reverse;
}
.align-right .chatscreen-arrow {
  width: 0;
  height: 0;
  margin-left: -1px;
  margin-right: unset;
  border-right: unset;
  border-left: 7px solid #1677ff;
  border-top: 7px solid transparent;
  border-bottom: 7px solid transparent;
  border-radius: 3px;
}
.chatscreen-arrow {
  width: 0;
  height: 0;
  margin-right: -1px;
  border-right: 7px solid #1677ff;
  border-top: 7px solid transparent;
  border-bottom: 7px solid transparent;
  border-radius: 3px;
}
.chatscreen-username-datetime {
  display: flex;
  align-items: center;
}
.chatscreen-message {
  background-color: #1677ff;
  border-radius: 6px;
  color: white;
  padding: 4px 15px;
  font-size: 14px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, 'Noto Sans', sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', 'Noto Color Emoji';
}
.chatscreen-username {
  font-size: 0.8rem;
  margin: 0 5px;
}
.chatscreen-datetime {
  font-size: 0.8rem;
  color: gray;
}
.chatscreen-messages {
  padding: 5px;
}
.chatscreen-chater {
  position: sticky;
  top: 0;
  background-color: white;
  border-bottom: 1px solid black;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 6px 0;
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