<template>
  <div>
    <a-form :form="form" style="max-width: 500px; margin: 40px auto 0;">
      <a-form-item
        label="应用案例"
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        class="stepFormText"
      >
        <Example
          ref="editor"
          v-decorator="[
            'example',
            {
              initialValue: { title: step.example.title, htmlContent: step.example.htmlContent },
              rules: [{ validator: checkExample }]
            }
          ]" />
      </a-form-item>

      <a-form-item :wrapperCol="{span: 19, offset: 5}">
        <a-button type="primary" @click="nextStep">下一步</a-button>
        <a-button style="margin-left: 8px" @click="prevStep">上一步</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
import Example from '@/components/Example/Example'
export default {
  name: 'Step2',
  components: {
    Example
  },
  data () {
    this.form = this.$form.createForm(this)
    return {
      labelCol: { lg: { span: 5 }, sm: { span: 5 } },
      wrapperCol: { lg: { span: 24 }, sm: { span: 24 } }
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
    },
    checkExample (rule, value, callback) {
      if (value.title === '' || value.title.length > 40) {
      // eslint-disable-next-line standard/no-callback-literal
        callback('案例标题不能为空且不能大于40个字')
        return
      } else if (value.htmlContent === '') {
      // eslint-disable-next-line standard/no-callback-literal
        callback('案例内容不能为空')
        return
      }
      callback()
    }
  }
}
</script>

<style lang="less" scoped>
.stepFormText {
  text-align: left;
  font-weight: bold;
  margin-bottom: 24px;

  .ant-form-item-label,
  .ant-form-item-control {
    line-height: 22px;
  }
}

</style>
