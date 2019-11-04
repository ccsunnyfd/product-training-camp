<template>
  <div>
    <!-- 已添加应用案例列表 -->
    <a-card style="max-width: 800px； margin: 40px auto 0;">
      <span style="font-weight: bold">
        已添加应用案例列表
      </span>
      <a-form :form="form">
        <a-form-item>
          <example-list
            ref="example-list"
            v-decorator="[
              'exampleList',
              {
                initialValue: step.exampleList,
                rules: [{ validator: checkExampleList }]
              }
            ]"
          ></example-list>
        </a-form-item>
        <a-form-item :wrapperCol="{span: 19, offset: 5}">
          <a-button type="primary" @click="nextStep">下一步</a-button>
          <a-button style="margin-left: 8px" @click="prevStep">上一步</a-button>
        </a-form-item>
      </a-form>
    </a-card>
    <!-- 富文本编辑器 -->
    <div style="max-width: 500px; margin: 40px auto 0;">
      <span>案例编辑器</span>
      <a-form :form="editorForm">
        <a-form-item>
          <example
            ref="editor"
            v-decorator="[
              'exObj',
              {
                initialValue: { title: exObj.title, htmlContent: exObj.htmlContent },
                rules: [{ validator: checkExample }]
              }
            ]"
          ></example>
        </a-form-item>
        <a-form-item :wrapperCol="{span: 19, offset: 5}">
          <a-button type="primary" @click="saveExample">保存案例</a-button>
        </a-form-item>
      </a-form>
    </div>

  </div>
</template>

<script>
import Example from '@/components/Example/Example'
import ExampleList from '@/components/Example/ExampleList'
let key = 1
export default {
  name: 'Step2',
  components: {
    Example,
    ExampleList
  },
  data () {
    this.form = this.$form.createForm(this)
    this.editorForm = this.$form.createForm(this, { name: 'editorForm' })
    return {
      labelCol: { lg: { span: 5 }, sm: { span: 5 } },
      wrapperCol: { lg: { span: 24 }, sm: { span: 24 } },
      exObj: {
        title: '',
        htmlContent: ''
      }
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
          // this.$refs.editor.removeDeletedImg()
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
    },
    checkExampleList (rule, value, callback) {
      if (value.length < 1) {
      // eslint-disable-next-line standard/no-callback-literal
        callback('至少填写一个案例')
        return
      }
      callback()
    },
    saveExample () {
      const { editorForm, $store } = this
      // 先校验，通过表单校验后，才进入下一步
      editorForm.validateFields((err, values) => {
        if (!err) {
          const nextKey = key++
          $store.commit({
            type: 'form/pushExample',
            payload: { ...values.exObj, key: nextKey }
          })
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
.stepFormText {
  margin-bottom: 24px;

  .ant-form-item-control {
    line-height: 22px;
  }
}
</style>
