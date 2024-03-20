import './assets/main.css'

import { createApp } from 'vue'
import { createStore } from 'vuex'
import App from './App.vue'
import router from './router'

const store = createStore({
  state () {
    return {
      socket: null,
      chater: '公共聊天室',
    }
  },
  mutations: {
    load (state,socket) {
      state.socket = socket;
    },
    changeChater (state,chater) {
      state.chater = chater;
    }
  }
});
const app = createApp(App);

app.use(router);
app.use(store);

app.mount('#app');
