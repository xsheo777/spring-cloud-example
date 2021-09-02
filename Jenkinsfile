podTemplate(
    cloud: "kubernetes",
    namespace: "kube-ops",
    label: "jnlp-slave",
    // 配置容器信息 image: "jenkins/jnlp-slave:4.6-1"
    containers: [
        containerTemplate(
            name: "jnlp",
            image: "cnych/jenkins:jnlp6"
        ),
    ],
    // 挂载，主要是为了使用宿主机的docker
    volumes: [
        hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock'),
        hostPathVolume(mountPath: '/usr/bin/docker', hostPath: '/usr/bin/docker'),
        hostPathVolume(mountPath: '/usr/bin/gradle', hostPath: '/usr/bin/gradle'),
        hostPathVolume(mountPath: '/root/.m2', hostPath: '/root/.m2')
    ]
) {
    parameters {
        choice (
            description: '模块名称', name: 'NAME', choices: ['spring-cloud-admin','spring-cloud-platform']
        )
    }
    node("jnlp-slave") {
        stage("clone") {
            echo "check out from GitSCM ========="
            checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: 'GIT', url: 'https://gitee.com/lyx_Engr/spring-cloud-example.git']]])
        }

        stage("build") {
            // build && push can use gradle-docker-plugin come true
            echo "start gradle build ========="
            sh '''
                cp -r /home/jenkins/agent/workspace/ /home/jenkins/workspace/
                cd /home/jenkins/agent/workspace/
                whoami
                ls -al /usr/local/bin | grep gradle
                gradle -v
                gradle ${params.NAME}:build
                cd ${params.Name} && docker build -t harbor.jkservice.org/dpa/spring-cloud-admin:v1 .
            '''
        }

        stage("push") {
            sh '''
                echo "4.Push Docker Image Stage"
                withCredentials([usernamePassword(credentialsId: 'dockerHub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
                    docker login -u ${dockerHubUser} -p ${dockerHubPassword}
                    docker push cnych/jenkins-demo:${build_tag}
                }
            '''
        }

        stage("clone") {
            echo "1.Clone Stage"
            sh "docker info"
        }

    }
}
