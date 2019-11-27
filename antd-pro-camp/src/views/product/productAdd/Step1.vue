<template>
  <div>
    <a-form :form="form" style="max-width: 500px; margin: 40px auto 0;">
      <a-form-item
        label="产品名称"
        :labelCol="labelCol"
        :wrapperCol="wrapperCol">
        <a-input v-decorator="['name', { initialValue: step.name, rules: [{required: true, min: 2, max: 20, message: '不少于2个字，最多20个字'}] }]"/>
      </a-form-item>
      <a-form-item
        label="产品简介"
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
      >
        <a-textarea
          :autosize="{ minRows: 2, maxRows: 5 }"
          v-decorator="['description', { initialValue: step.description, rules: [{required: true, max: 250, message: '必填，字数不超过250字'}] }]"/>
      </a-form-item>
      <a-form-item
        label="适用场景和人群"
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
      >
        <a-textarea
          :autosize="{ minRows: 2, maxRows: 5 }"
          v-decorator="['scenario', { initialValue: step.scenario, rules: [{required: true, max: 250, message: '必填，字数不超过250字'}] }]"/>
      </a-form-item>
      <a-form-item
        label="首页产品图标"
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
      >
        <img-uploader ref="favicon" :modal="cropDom" :id="1" v-decorator="['favicon', { initialValue: step.favicon, rules: [{required: true, message: '请上传首页产品图标'}] }]" />
      </a-form-item>
      <a-form-item
        label="产品详情页图片"
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
      >
        <img-uploader ref="prodImg" :modal="cropDom" :id="2" v-decorator="['prodImg', { initialValue: step.prodImg, rules: [{required: true, message: '请上传产品详情页图片'}] }]" />
      </a-form-item>
      <a-form-item
        label="左侧划栏的图标"
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
      >
        <font-icon-selector v-decorator="['iconType', { initialValue: step.iconType, rules: [{required: true, message: '请选择左侧划栏的图标'}] }]" />

      </a-form-item>
      <a-form-item :wrapperCol="{span: 19, offset: 5}">
        <a-button type="primary" @click="nextStep">下一步</a-button>
      </a-form-item>
    </a-form>
    <a-divider />
    <div class="step-form-style-desc">
      <h3>说明</h3>
      <h4>填写产品基本信息</h4>
      <p>如果需要，这里可以放一些关于产品的常见问题说明。如果需要，这里可以放一些关于产品的常见问题说明。如果需要，这里可以放一些关于产品的常见问题说明。</p>
    </div>

    <crop-modal ref="modal" :autoCropWidth="option.autoCropWidth" :autoCropHeight="option.autoCropHeight" @ok="setImg"/>
  </div>
</template>

<script>
import { CropModal, ImgUploader } from '@/components/CropUpload'
import FontIconSelector from '@/components/IconSelector/FontIconSelector'

export default {
  name: 'Step1',
  components: {
    CropModal,
    ImgUploader,
    FontIconSelector
  },
  mounted () {
    this.$nextTick(() => {
      this.cropDom = this.$refs.modal
    })
  },
  data () {
    this.form = this.$form.createForm(this)
    return {
      cropDom: {},
      labelCol: { lg: { span: 8 }, sm: { span: 8 } },
      wrapperCol: { lg: { span: 16 }, sm: { span: 16 } },
      // cropper
      preview: {},
      option: {
        // img: '/avatar2.jpg',
        info: true,
        size: 1,
        outputType: 'jpeg',
        canScale: false,
        autoCrop: true,
        // 只有自动截图开启 宽度高度才生效
        autoCropWidth: 180,
        autoCropHeight: 180,
        fixedBox: true,
        // 开启宽度和高度比例
        fixed: true,
        fixedNumber: [1, 1]
      }
      // iconSelector
      // currentSelectedIcon: 'pause-circle'
    }
  },
  computed: {
    step () {
      return this.$store.state.form.step
    }
  },
  methods: {
    // cropUpload
    setImg (id, url) {
      if (id === 1) {
        this.$refs.favicon.$emit('change', url)
      } else if (id === 2) {
        this.$refs.prodImg.$emit('change', url)
      }
    },
    nextStep () {
      const { form, $store } = this
      // 先校验，通过表单校验后，才进入下一步
      form.validateFields((err, values) => {
        if (!err) {
          $store.commit({
            type: 'form/saveStepFormData',
            payload: values
          })
          this.$emit('nextStep')
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
.step-form-style-desc {
  padding: 0 56px;
  color: rgba(0,0,0,.45);

  h3 {
    margin: 0 0 12px;
    color: rgba(0,0,0,.45);
    font-size: 16px;
    line-height: 32px;
  }

  h4 {
    margin: 0 0 4px;
    color: rgba(0,0,0,.45);
    font-size: 14px;
    line-height: 22px;
  }

  p {
    margin-top: 0;
    margin-bottom: 12px;
    line-height: 22px;
  }

  .avatar-upload-wrapper {
    height: 200px;
    width: 100%;
  }
}
</style>
