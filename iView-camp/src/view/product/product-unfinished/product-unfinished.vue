<template>
  <div>unfinished</div>
</template>
<script>
export default {
  name: 'ProductUnfinishedPage',
  data() {
    return {
      label: h => {
        return h('div', [
          h('span', '未完成'),
          h('Badge', {
            props: {
              count: 3,
            },
          }),
        ]);
      },
    };
  },
  methods: {
    //
  },
  mounted() {
    //
  },
};
</script>
