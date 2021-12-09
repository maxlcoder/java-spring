package pers.maxlcoder.demo;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pers.maxlcoder.demo.service.A;
import pers.maxlcoder.demo.service.B;


@Configuration
@ComponentScan
public class Main {

    public static void main(String[] args) {
        // @Configuration 定义当前类是配置类，@ComponentScan 定义 Bean 扫描是扫描当前配置类所在的 Package，
        // 这个和通过 xml 文件来加载 Bean 结果是相同的，但是注解的形式更简洁。
        // new AnnotationConfigApplicationContext(Main.class) 表示从读取当前类的配置信息，也就是注解的启动类
        // 这里 Main.class 实际可以换成任意的标注了 @Configuration 和 @ComponentScan 的类 （测试的结果），目的就是
        // 读取注解配置生成 IoC 容器，至于这个注解在那个文件无所谓，前提是包要对的上

        // 在 Class C 中，通过注解 @Component 表明当前 Class C 是一个 Bean ，也就可以通过容器方法 getBean 来实例化这个 C 类
        // 注意这里的 C 类又一个 @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) ，表明这个一个原型 Bean ,和默认 Bean 的
        // 差别是，默认Bean 是 IoC 容器初始化时就创建了而且是一个单例 （getBean 总是获取的是同一个实例），因为这里我在 C 类的构造函数里面
        // 传了参数，这里需要定义原型 Bean ，表明创建的不是一个单例，下次 getBean 传参时，创建的是一个新的实例

        // 可以看到整个调用过程，创建 IoC 容器，Ioc 容器来实例化类
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        C cService = context.getBean(C.class, new Object[] {"ggg"});
        String cName = cService.getName();
        System.out.println(cName);

    }
}

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class C {
    private String name;

    public C(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
