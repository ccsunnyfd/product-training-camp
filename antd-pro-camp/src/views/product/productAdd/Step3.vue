<template>
  <div>
    <a-form :form="form" style="max-width: 500px; margin: 40px auto 0;">
      <a-form-item
        label="课时上传"
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
      >
        <upload-video
          :maxNum="1"
          :uploadAction="uploadAction"
          v-decorator="[
            'course',
            {
              initialValue: { title: step.course.title, videoTrail: step.course.videoTrail },
              rules: [{ validator: checkVideoUpload }]
            }
          ]" />
      </a-form-item>
      <a-form-item :wrapperCol="{span: 19, offset: 5}">
        <a-button :loading="loading" type="primary" @click="nextStep">提交</a-button>
        <a-button style="margin-left: 8px" @click="prevStep">上一步</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
import UploadVideo from '@/components/UploadVideo/UploadVideo'

export default {
  name: 'Step2',
  components: {
    UploadVideo
  },
  data () {
    this.form = this.$form.createForm(this)
    return {
      labelCol: { lg: { span: 5 }, sm: { span: 5 } },
      wrapperCol: { lg: { span: 19 }, sm: { span: 19 } },
      loading: false,
      timer: 0,
      uploadAction: '/api/uploadStream'
    }
  },
  computed: {
    step () {
      return this.$store.state.form.step
    }
  },
  methods: {
    nextStep () {
      const that = this
      const { form, $store, step } = this
      that.loading = true
      form.validateFields((err, values) => {
        if (!err) {
          that.timer = setTimeout(function () {
            that.loading = false
            $store.dispatch({
              type: 'form/submitStepForm',
              payload: { ...step, ...values }
            })
            that.$emit('nextStep')
          }, 1500)
        } else {
          that.loading = false
        }
      })
    },
    prevStep () {
      this.$emit('prevStep')
    },
    checkVideoUpload (rule, value, callback) {
      if (value.title === '' || value.title.length > 40) {
      // eslint-disable-next-line standard/no-callback-literal
        callback('课时标题不能为空且不能大于40个字')
        return
      } else if (value.videoTrail === '') {
      // eslint-disable-next-line standard/no-callback-literal
        callback('请上传课时视频')
        return
      }
      callback()
    }
  },
  beforeDestroy () {
    clearTimeout(this.timer)
  }
}
</script>

<style lang="less" scoped>
  .stepFormText {
    margin-bottom: 24px;

    .ant-form-item-label,
    .ant-form-item-control {
      line-height: 22px;
    }
  }

</style>
