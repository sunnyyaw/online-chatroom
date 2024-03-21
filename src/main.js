import './assets/main.css'
import 'ant-design-vue/dist/reset.css'

import { createApp } from 'vue'
import { createStore } from 'vuex'
import App from './App.vue'
import Antd from 'ant-design-vue';
import router from './router'


const store = createStore({
  state () {
    const username = localStorage.getItem('username');
    const message = JSON.parse(localStorage.getItem(`${username}message`));
    const token = localStorage.getItem('token');
    return {
      socket: null,
      chater: '公共聊天室',
      baseURL: 'http://localhost:8080',
      username: username,
      messages: message ? message : {},
      token: token,
    }
  },
  mutations: {
    load (state,socket) {
      state.socket = socket;
    },
    changeChater (state,chater) {
      state.chater = chater;
    },
    pushMessage (state,payload) {
      const msg = payload.msg;
      const channel = payload.channel;
      if (!state.messages[channel]) {
        state.messages[channel] = {
          messages: [msg],
          unread: 0
        };
        if (state.chater !== channel ){
          state.messages[channel].unread = 1;
        }
      } else {
        state.messages[channel].messages.push(msg);
        if (state.chater !== channel) {
          state.messages[channel].unread += 1;
        }
      }
    },
    storeMessage (state) {
      localStorage.setItem(`${state.username}message`,JSON.stringify(state.messages));
    },
    storeToken (state,token) {
      state.token = token;
      localStorage.setItem('token',token);
    },
    storeUsername (state,username) {
      state.username = username;
      localStorage.setItem('username',username);
    },
    loadMessage (state) {
      const messages = JSON.parse(localStorage.getItem(`${state.username}message`));
      state.messages = messages ? messages : {};
    },
    clearUnread (state,channel) {
      if (state.messages[channel]) {
        state.messages[channel].unread = 0;
      }
      localStorage.setItem(`${state.username}message`,JSON.stringify(state.messages));
    }
  }
});
const app = createApp(App);

app.use(router);
app.use(store);
app.use(Antd);

app.mount('#app');
