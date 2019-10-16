<template>
  <div>
    <Tabs type="card" value="release">
      <TabPane label="发布" name="release">
        <router-view name="productRelease" />
      </TabPane>
      <TabPane label="管理" name="management">
        <router-view name="productManagement" />
      </TabPane>
      <TabPane :label="label" name="unfinished">
        <router-view name="productUnfinished" />
      </TabPane>
    </Tabs>
  </div>
</template>
<script>
export default {
  name: 'ProductPage',
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
    this.$router.push('/product/product_page/product_tab_page');
  },
};
</script>
