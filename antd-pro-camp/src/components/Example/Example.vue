<template>
  <div>
    <div>
      <span>案例标题：</span>
      <a-input
        type="text"
        v-model="title"
        @change="handleTitleChange"
        style="width: 80%;"
      />
    </div>
    <div>
      <span>案例内容：</span>
      <Teditor
        ref="editor"
        v-model="htmlContent"
        @change="handleContentChange"/>
    </div>
  </div>

</template>
<script>
import Teditor from '@/components/Editor/Teditor'

export default {
  name: 'Example',
  components: {
    Teditor
  },
  props: {
    value: {
      type: Object,
      required: true
    }
  },
  watch: {
    value (val) {
      Object.assign(this, val)
    }
  },
  data () {
    const { title, htmlContent } = this.value || {}
    return {
      title: title || '',
      htmlContent: htmlContent || ''
    }
  },
  methods: {
    removeDeletedImg () {
      this.$refs.editor.removeDeletedImg()
    },
    handleTitleChange (e) {
      const newVal = e.target.value
      this.$emit('change', { ...this.value, title: newVal })
    },
    handleContentChange (content) {
      this.$emit('change', { ...this.value, htmlContent: content })
    }
  }
}
</script>
