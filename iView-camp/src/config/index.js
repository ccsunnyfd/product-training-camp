export default {
  /**
   * @description 配置显示在浏览器标签的title
   */
  title: 'ProductCamp-admin',
  /**
   * @description token在Cookie中存储的天数，默认1天
   */
  cookieExpires: 1,
  /**
   * @description 是否使用国际化，默认为false
   *              如果不使用，则需要在路由中给需要在菜单中展示的路由设置meta: {title: 'xxx'}
   *              用来在菜单中显示文字
   */
  useI18n: true,
  /**
   * @description api请求基础路径
   */
  baseUrl: {
    dev: 'https://localhost:8080/api/',
    pro: 'https://produce.com'
  },
  /**
   * @description api请求基础路径
   */
  s3Url: {
    dev: 'http://172.16.70.2:9000/',
    pro: 'https://oss.aliyun.com/'
  },
  /**
   * @description 默认打开的首页的路由name值，默认为home
   */
  homeName: 'home',
  /**
   * @description 需要加载的插件
   */
  plugin: {
    'error-store': {
      showInHeader: true, // 设为false后不会在顶部显示错误日志徽标
      developmentOff: true // 设为true后在开发环境不会收集错误信息，方便开发中排查错误
    }
  },
  iconsForChosen: [
    'icon-yuncunchu',
    'icon-cunchushebei',
    'icon-yidongyunkongzhitaiicon06',
    'icon-exam',
    'icon-chart14',
    'icon-chart18',
    'icon-chart34',
    'icon-chart38',
    'icon-circle',
    'icon-chart12',
    'icon-chart58',
    'icon-chart78',
    'icon-fenbushicunchu',
    'icon-fangdajing',
    'icon-Box',
    'icon-fenbushicunchu1',
    'icon-shouye',
    'icon-wenhao',
    'icon-jindudengdai',
    'icon-rongzaitubiao',
    'icon-m_user_setting',
    'icon-mulu',
    'icon-duixiangcunchu',
    'icon--',
    'icon-kaoshi',
    'icon--1',
    'icon-jjj',
    'icon-lieshicunchu',
    'icon-chaorongheshujuzhongxin',
    'icon-jjj1',
    'icon-jjj2',
    'icon-chengji',
    'icon-iconfontyoujiantou',
    'icon-rongzaidengji',
    'icon-yunzhuomian',
    'icon-chengji1',
    'icon-ziyuan',
    'icon-chengji2',
    'icon-fenbushicunchu2',
    'icon-wodedangxuan',
    'icon-yunwei',
    'icon-rongzaibeifen',
    'icon-Database_maintain',
    'icon-chengji3',
    'icon-chengji4',
    'icon-settingswindow',
    'icon-chanpinguanli',
    'icon-chengji5',
    'icon-kaoshi2',
    'icon-rongzai',
    'icon-fenbushicunchu3',
    'icon-wodedangxuan-copy',
    'icon-yunrongzaiInCDR',
    'icon-daohang_jiantou_zuo_dingbu',
    'icon-productManage-copy'
  ]
}
