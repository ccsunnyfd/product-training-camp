<template>
  <Card>
    <!-- 步骤条 -->
    <Steps class="margin-left-80" :current="currentStep">
      <Step v-for="item in stepList" :key="item.key" :title="item.title" :content="item.content"></Step>
    </Steps>

    <!-- 表单 -->
    <i-form class="margin-top-40 padding-40" ref='basicInfo' :model="basicInfo" :rules="ruleValidate" :label-width="130">
      <Form-item label="产品名称" prop="name">
        <i-input v-model="basicInfo.name" placeholder="不少于2个字，最多20个字"></i-input>
      </Form-item>
      <Form-item label="产品简介" prop="description">
        <i-input v-model="basicInfo.description" type="textarea" :autosize="{minRows: 2,maxRows: 5}" placeholder="字数不超过250字"></i-input>
      </Form-item>
      <Form-item label="适用场景和人群" prop="scenario">
        <i-input v-model="basicInfo.scenario" type="textarea" :autosize="{minRows: 2,maxRows: 5}" placeholder="字数不超过250字"></i-input>
      </Form-item>
      <Row>
        <i-col span="8">
          <!-- 首页产品图标 -->
          <Form-item label="首页产品图标" prop="favicon">
            <Cavatar :fileinfo='basicInfo.favicon' :cropWidth=200 :cropHeight=200 @imgUpdate='handleFaviconUpdate'></Cavatar>
          </Form-item>
        </i-col>
        <i-col span="8">
          <Form-item label="产品详情页图片" prop="prodImg">
            <Cavatar :fileinfo='basicInfo.prodImg' :cropWidth=300 :cropHeight=200 @imgUpdate='handleProdImgUpdate'></Cavatar>
          </Form-item>
        </i-col>
        <i-col span="8">
          <Form-item label="左侧划栏的图标" prop="iconType">
            <CchooseIcon :iconinfo='basicInfo.iconType' @iconUpdate='handleIconUpdate'></CchooseIcon>
          </Form-item>
        </i-col>
      </Row>

      <!-- <Form-item>
        <i-button type="primary" @click="handleSubmit('basicInfo')">提交</i-button>
      </Form-item> -->
    </i-form>
    <!-- 下一步及提交按钮 -->
    <div style="text-align: center;">
      <Button type="primary" :disabled="nextStepButton.disabled" @click="next" :loading="loading">{{nextStepButton.value}}</Button>
    </div>
  </Card>
</template>

<script>
import Cavatar from '_c/cAvatar'
import CchooseIcon from '_c/cChooseIcon'
import { submitBasicInfo } from '@/api/data.js'
export default {
  name: 'product_release_page',
  components: {
    Cavatar,
    CchooseIcon
  },
  data () {
    return {
      currentStep: 0,
      nextStepButton: {
        disabled: false,
        value: '下一步'
      },
      stepList: [
        {
          key: 'step_0',
          title: '进行中',
          content: '基本信息'
        },
        {
          key: 'step_1',
          title: '待进行',
          content: '应用实例'
        },
        {
          key: 'step_2',
          title: '待进行',
          content: '课程上传'
        },
        {
          key: 'step_3',
          title: '待进行',
          content: '关联考试'
        }
      ],
      basicInfo: {
        name: '',
        description: '',
        scenario: '',
        favicon: '',
        iconType: '',
        prodImg: ''
      },
      ruleValidate: {
        name: [
          { required: true, message: '请输入产品名称', trigger: 'blur' },
          { min: 2, message: '介绍不能少于2个字', trigger: 'blur' },
          { max: 20, message: '介绍不能多于20字', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入产品简介', trigger: 'blur' },
          { max: 250, message: '字数不超过250字', trigger: 'blur' }
        ],
        scenario: [
          { required: true, message: '请输入适用场景和人群', trigger: 'blur' },
          { max: 250, message: '字数不超过250字', trigger: 'blur' }
        ],
        favicon: [
          { required: true, message: '请选择首页图标', trigger: 'change' }
        ],
        iconType: [
          { required: true, message: '请选择侧滑栏的小图标', trigger: 'change' }
        ],
        prodImg: [
          { required: true, message: '请选择产品详情页图片', trigger: 'change' }
        ]
      },
      loading: false
    }
  },
  methods: {
    handleFaviconUpdate (url) {
      this.basicInfo.favicon = url
    },
    handleProdImgUpdate (url) {
      this.basicInfo.prodImg = url
    },
    handleIconUpdate (iconType) {
      this.basicInfo.iconType = iconType
    },
    next () {
      const submitRes = this.handleSubmit('basicInfo')
      if (!submitRes) {
        return
      }
      this.stepList[this.currentStep].title = '已完成'
      if (this.currentStep === 2) {
        this.nextStepButton.value = '提交'
      } else if (this.currentStep === 3) {
        this.nextStepButton.disabled = true
      }
      if (this.currentStep !== 3) {
        this.currentStep += 1
        this.stepList[this.currentStep].title = '进行中'
      }
    },
    handleSubmit (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.loading = true
          submitBasicInfo(this.basicInfo).then((resp) => {
            if (resp.data.status === 'success') {
              this.loading = false
              this.$Message.success('提交成功~', 3)
            } else {
              this.$Message.warning('提交失败，请联系管理员~', 3)
              this.loading = false
            }
          }).catch(err => {
            this.$Message.warning('提交失败，请联系管理员~' + err, 5)
            this.loading = false
          })
          return true
        } else {
          this.$Message.error('表单验证失败!', 3)
          return false
        }
      })
    }
    // handleReset (name) {
    //   this.$refs[name].resetFields()
    // }
  },
  mounted () {
  }
}
</script>

<style lang="less">
  @import './product-release.less';
</style>
