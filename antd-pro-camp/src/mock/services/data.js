import Mock from 'mockjs2'

const uploadStream = {
  status: 'success',
  msg: '上传form-data成功！',
  url: Mock.mock('@integer(0, 1)') > 0.5 ? 'https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.png' : 'https://img.alicdn.com/tfs/TB1kyVhkkT2gK0jSZFkXXcIQFXa-282-282.jpg'
}

const uploadVideo = {
  status: 'success',
  msg: '上传视频成功！',
  videoList: [
    {
      name: 'FalconStor Deduplication Optimizes Backup for Big Data and Cloud Storage',
      url: 'http://172.16.70.2:9000/product-camp/test/video/FalconStor%20Deduplication%20Optimizes%20Backup%20for%20Big%20Data%20and%20Cloud%20Storage%20Strategies.mp4?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=ZWHVQOKYOVTXSU9WH3CW%2F20191101%2F%2Fs3%2Faws4_request&X-Amz-Date=20191101T011531Z&X-Amz-Expires=432000&X-Amz-SignedHeaders=host&X-Amz-Signature=b8b8ea6de4af98882a4ec7c95f604ec5852e899a43c483ea6e5c53bccdb65d5a'
    },
    {
      name: '柏科数据容灾系统快速恢复平台介绍',
      url: 'http://172.16.70.2:9000/product-camp/test/video/%E6%9F%8F%E7%A7%91%E6%95%B0%E6%8D%AE%E5%AE%B9%E7%81%BE%E7%B3%BB%E7%BB%9F%E5%BF%AB%E9%80%9F%E6%81%A2%E5%A4%8D%E5%B9%B3%E5%8F%B0%E4%BB%8B%E7%BB%8D.mov?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=ZWHVQOKYOVTXSU9WH3CW%2F20191101%2F%2Fs3%2Faws4_request&X-Amz-Date=20191101T011603Z&X-Amz-Expires=432000&X-Amz-SignedHeaders=host&X-Amz-Signature=ab3320986fb56d072c9fc049845e1f8c6f62222321026ab31f96c7032ab346ce'
    },
    {
      name: '恢复平台介绍',
      url: 'http://172.16.70.2:9000/product-camp/test/video/%E%9F%8F%E7%A7%91%E6%95%B0%E6%8D%AE%E5%AE%B9%E7%81%BE%E7%B3%BB%E7%BB%9F%E5%BF%AB%E9%80%9F%E6%81%A2%E5%A4%8D%E5%B9%B3%E5%8F%B0%E4%BB%8B%E7%BB%8D.mov?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=ZWHVQOKYOVTXSU9WH3CW%2F20191101%2F%2Fs3%2Faws4_request&X-Amz-Date=20191101T011603Z&X-Amz-Expires=432000&X-Amz-SignedHeaders=host&X-Amz-Signature=ab3320986fb56d072c9fc049845e1f8c6f62222321026ab31f96c7032ab346ce'
    }
  ]
}

const removeObject = {
  status: 'success',
  msg: '删除s3资源成功！'
}

const productAddForm = {
  status: 'success',
  msg: '添加新产品成功！'
}

Mock.mock(/\/api\/uploadStream/, 'post', uploadStream)
Mock.mock(/\/api\/removeObject/, 'post', removeObject)
Mock.mock(/\/api\/product\/addForm/, 'post', productAddForm)
Mock.mock(/\/api\/uploadVideo/, 'post', uploadVideo)
