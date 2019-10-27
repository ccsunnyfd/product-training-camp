import { axios } from '@/utils/request'

const state = {
  step: {
    name: '',
    description: '',
    scenario: '',
    favicon: '',
    prodImg: '',
    iconType: '',
    exampleTitle: '',
    exampleHtmlContent: '',
    course: {
      title: '',
      videoTrail: ''
    }
  }
}

const actions = {
  async submitStepForm ({ commit }, { payload }) {
    await axios.request({
      url: '/api/productAddForm',
      method: 'POST',
      data: payload
    })
    commit('saveStepFormData', { payload })
  }
}

const mutations = {
  saveStepFormData (state, { payload }) {
    console.log(payload)
    state.step = {
      ...state.step,
      ...payload
    }
  }
}

export default {
  namespaced: true,
  state,
  actions,
  mutations
}
