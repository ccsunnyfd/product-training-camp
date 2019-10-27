<template>
  <div>
    <a-form :form="form" style="max-width: 500px; margin: 40px auto 0;">
      <a-form-item
        label="应用案例标题"
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        class="stepFormText"
      >
        <a-input
          style="width: 80%;"
          v-decorator="['exampleTitle', { initialValue: step.exampleTitle, rules: [{required: true, max: 40, message: '必填，最多40个字'}] }]" />
      </a-form-item>
      <a-form-item
        label="案例内容"
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        class="stepFormText"
      >
        <Teditor ref="editor" v-decorator="['exampleHtmlContent', { initialValue: step.exampleHtmlContent, rules: [{required: true, message: '必填'}] }]" />
      </a-form-item>
      <a-form-item :wrapperCol="{span: 19, offset: 5}">
        <a-button type="primary" @click="nextStep">下一步</a-button>
        <a-button style="margin-left: 8px" @click="prevStep">上一步</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
import Teditor from '@/components/Editor/Teditor'
export default {
  name: 'Step2',
  components: {
    Teditor
  },
  data () {
    this.form = this.$form.createForm(this)
    return {
      labelCol: { lg: { span: 5 }, sm: { span: 5 } },
      wrapperCol: { lg: { span: 19 }, sm: { span: 19 } },
      loading: false,
      timer: 0
    }
  },
  computed: {
    step () {
      return this.$store.state.form.step
    }
  },
  methods: {
    nextStep () {
      const { form, $store, step } = this
      // 先校验，通过表单校验后，才进入下一步
      form.validateFields((err, values) => {
        if (!err) {
          // 向服务器请求删除delete掉的图片
          this.$refs.editor.removeDeletedImg()
          $store.commit({
            type: 'form/saveStepFormData',
            payload: { ...step, ...values }
          })
          this.$emit('nextStep')
        }
      })
    },
    prevStep () {
      this.$emit('prevStep')
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
