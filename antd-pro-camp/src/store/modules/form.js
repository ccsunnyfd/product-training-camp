import { submitStepForm } from '@/api/data.js'

const state = {
  step: {
    name: '',
    description: '',
    scenario: '',
    favicon: '',
    prodImg: '',
    iconType: '',
    exampleList: [],
    courseList: []
  }
}

const actions = {
  async submitStepForm ({ commit }, { payload }) {
    await submitStepForm(payload)
    // commit('saveStepFormData', payload)
    commit('clearData')
  }
}

const mutations = {
  pushExample (state, { payload }) {
    state.step.exampleList.push(payload)
  },
  clearData (state) {
    state.step = {
      name: '',
      description: '',
      scenario: '',
      favicon: '',
      prodImg: '',
      iconType: '',
      exampleList: [],
      courseList: []
    }
  },
  saveStepFormData (state, { payload }) {
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
