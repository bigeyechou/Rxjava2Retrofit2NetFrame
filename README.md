
### 项目介绍
    NetWorkFrame：Android网络请求框架

### 软件架构
    基于Retrofit+Rxjava+Okhttp封装的网络请求框架
    使用时请自行修改okhttp的公共参数（HeaderParam）
    1.HttpService请求的接口
    2.HttpMethods类：初始化并配置Retrofit和OkHttp
    3.OnSuccessAndFaultSub类：封装回调,项目中接收的是gzip压缩过的流
    4.各种Api类：根据业务模块划分，实现观察者和被观察者的订阅。
    
### 使用说明
    可直接使用
    集成的接口获取的数据需要在OnSuccessAndFaultSub.class文件的onNext方法看（因为接口只是为了测试Retrofit，框架是根据自己公司的接口规则封装的）
    详情使用介绍请查看简书：https://www.jianshu.com/p/0ad99e598dba  
    喜欢点赞支持一下
