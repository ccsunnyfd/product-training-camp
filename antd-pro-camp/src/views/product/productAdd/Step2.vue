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
          <span>案例标题：</span>
          <a-input
            type="text"
            style="width: 80%;"
            v-model="title"
          />
          <span v-show="!titleValid" style="display: block; color: red">案例标题不能为空且不能大于40个字</span>
        </a-form-item>
        <a-form-item>
          <span>案例内容：</span>
          <Teditor
            ref="editor"
            v-model="htmlContent"
          />
          <span v-show="!contentValid" style="display: block; color: red">案例内容不能为空</span>
        </a-form-item>
        <a-form-item :wrapperCol="{span: 19, offset: 5}">
          <a-button type="primary" :disabled="!titleValid || !contentValid" @click="saveExample">保存案例</a-button>
        </a-form-item>
      </a-form>
    </div>

  </div>
</template>

<script>
import Teditor from '@/components/Editor/Teditor'
// import Example from '@/components/Example/Example'
import ExampleList from '@/components/Example/ExampleList'
let key = 1
export default {
  name: 'Step2',
  components: {
    // Example,
    ExampleList,
    Teditor
  },
  data () {
    this.form = this.$form.createForm(this)
    this.editorForm = this.$form.createForm(this, { name: 'editorForm' })
    return {
      labelCol: { lg: { span: 5 }, sm: { span: 5 } },
      wrapperCol: { lg: { span: 24 }, sm: { span: 24 } },
      title: '',
      htmlContent: ''
    }
  },
  computed: {
    step () {
      return this.$store.state.form.step
    },
    titleValid () {
      return !(this.title === '' || this.title.length > 40)
    },
    contentValid () {
      return !(this.htmlContent === '')
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
    checkExampleList (rule, value, callback) {
      if (value.length < 1) {
      // eslint-disable-next-line standard/no-callback-literal
        callback('至少填写一个案例')
        return
      }
      callback()
    },
    saveExample () {
      const editor = this.$refs.editor
      const { $store } = this
      // 先校验，通过表单校验后，才进入下一步
      if (this.titleValid && this.contentValid) {
        editor.removeDeletedImg()
        const nextKey = key++
        $store.commit({
          type: 'form/pushExample',
          payload: { title: this.title, htmlContent: this.htmlContent, key: nextKey }
        })
        this.title = ''
        this.htmlContent = ''
        editor.resetPostedImg()
      }
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
