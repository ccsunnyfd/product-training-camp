<template>
  <a-card :bordered="false">
    <a-steps class="steps" :current="currentTab">
      <a-step title="填写产品基本信息" />
      <a-step title="填写应用案例" />
      <a-step title="上传课程视频" />
      <a-step title="完成" />
    </a-steps>
    <div class="content">
      <step1 v-if="currentTab === 0" @nextStep="nextStep"/>
      <step2 v-if="currentTab === 1" @nextStep="nextStep" @prevStep="prevStep"/>
      <step3 v-if="currentTab === 2" @nextStep="nextStep" @prevStep="prevStep"/>
      <step4 v-if="currentTab === 3" @prevStep="prevStep" @finish="finish"/>
    </div>
  </a-card>
</template>

<script>
import Step1 from './Step1'
import Step2 from './Step2'
import Step3 from './Step3'
import Step4 from './Step4'
import getCurrentProduct from '@/views/product/utils/getCurrentProduct'

export default {
  name: 'StepForm',
  components: {
    Step1,
    Step2,
    Step3,
    Step4
  },
  data () {
    return {
      description: '将一个冗长或用户不熟悉的表单任务分成多个步骤，指导用户完成。',
      currentTab: 0,

      // form
      form: null
    }
  },
  mounted () {
    this.setProductInForm()
  },
  methods: {
    // setProductInForm
    async setProductInForm () {
      const id = this.$route.query.id
      let currentItem = {}
      await getCurrentProduct(id).then((results) => {
        const basicInfo = results[0].data
        const exampleList = results[1].data
        const courseList = results[2].data
        currentItem = { ...basicInfo, ...{ exampleList }, ...{ courseList } }
      })
      const { $store } = this
      await $store.commit({
        type: 'form/clearData'
      })
      $store.commit({
        type: 'form/saveStepFormData',
        payload: currentItem
      })
    },
    // handler
    nextStep () {
      if (this.currentTab < 3) {
        this.currentTab += 1
      }
    },
    prevStep () {
      if (this.currentTab > 0) {
        this.currentTab -= 1
      }
    },
    finish () {
      this.currentTab = 0
    }
  }
}
</script>

<style lang="less" scoped>
  .steps {
    max-width: 750px;
    margin: 16px auto;
  }
</style>
