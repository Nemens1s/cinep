Windows set-up

1. Open PuttyGen to change format of private keys from .pem to .ppk
1.1 Browse to .pem file and press create private key

2 Open Putty. 
2.1 In host name field insert ubuntu@(public dns), port stays 22.
2.2 In the Category pane, expand Connection, expand SSH, and then choose Auth
2.2.1 Choose Browse.
2.2.2 Select the .ppk file that you generated for your key pair and choose Open.
2.2.3 Press yes all the time

3 Git-lab runner
  3.1 Installing gitlab runner
  3.1.1 In putty when you are in use these commands:
        sudo curl -L --output /usr/local/bin/gitlab-runner https://gitlab-runner-downloads.s3.amazonaws.com/latest/binaries/gitlab-runner-linux-amd64
        Permissions to execute:
        sudo chmod +x /usr/local/bin/gitlab-runner
        Creating GitLab CI user:
        sudo useradd --comment 'GitLab Runner' --create-home gitlab-runner --shell /bin/bash
        Installing and running as service:
        sudo gitlab-runner install --user=gitlab-runner --working-directory=/home/gitlab-runner
        sudo gitlab-runner start
  3.2 Registering a runner
  3.2.1 sudo gitlab-runner register
        Enter Gitlab instance URL:
        https://gitlab.cs.ttu.ee/
        Then registration token. To get it go to your project in gitlab
        Settings-> CI/CD -> locate Runners -> Click expand -> Under "set up a specific runner manually" copy token.
        Enter description:
        For example cinep
        Enter tags:
        For example cinep-back
        Runner executor:
        Shell
        
  3.3
    Configure .gitlab-ci.yml file in project root folder
    """
    stages:
      - build
      - test
      - deploy         
    before_script:
      - chmod +x ./gradlew
      - export GRADLE_USER_HOME=`pwd`/.gradle    
    build cinep:
      stage: build
      cache:
        paths:
          - .gradle/wrapper
          - .gradle/caches
      artifacts:
        paths:
          - build/libs
      tags:
        - cinep-back
      script:
        - ./gradlew clean assemble --no-daemon    
    test cinep:
      stage: test
      tags:
        - cinep-back
      script:
        - ./gradlew check --stacktrace --no-daemon  
    deploy cinep:
      stage: deploy
      tags:
        - cinep-back
      script:
        - mkdir -p ~/api-deployment
        - rm -rf ~/api-deployment/*
        - cp -r build/libs/. ~/api-deployment
        - sudo service cinep restart
       """ 
     Also, you can add when job triggers, add it after script in job that you want
     currently it will trigger only when push is made to master branchude37yef                                                           
     only:
         refs:
           - master
           
        
4 Installing Java to server
    sudo apt-get update
    sudo apt-get install openjdk-11-jre
    sudo apt-get install openjdk-11-jdk
    sudo apt-get update
    
    
5 Defining back-end as linux service.
    cd /etc/systemd/system/
    sudo touch cinep.service
    """
    [Unit]
    Description=cinep-back service
    After=network.target
    [Service]
    Type=simple
    User=gitlab-runner
    WorkingDirectory=/home/gitlab-runner/api-deployment
    ExecStart=/usr/bin/java -jar cinep-back.jar
    Restart=on-abort
    [Install]
    WantedBy=multi-user.target  
    """
    sudo systemctl daemon-reload   
    sudo systemctl enable heroes  
    sudo service heroes restart   
    Restarting service after build   
    sudo visudo
    add to the end of file:
    gitlab-runner ALL = NOPASSWD: /usr/sbin/service cinep *
    
6 Installing database to server:
    sudo apt update
    sudo apt -y install vim bash-completion wget
    sudo apt -y upgrade
    sudo reboot
    ------------
    wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -
    echo "deb http://apt.postgresql.org/pub/repos/apt/ `lsb_release -cs`-pgdg main" |sudo tee  /etc/apt/sources.list.d/pgdg.list
    sudo apt update
    sudo apt -y install postgresql-12 postgresql-client-12
   
   6.1 Check connection
    sudo su - postgres
    psql -c "alter user postgres with password 'StrongAdminP@ssw0rd'"
    Start PostgreSQL prompt by using the command:
    psql
    \conninfo
    6.2 Create db and user
        CREATE DATABASE mytestdb;
        CREATE USER mytestuser WITH ENCRYPTED PASSWORD 'MyStr0ngP@SS';
        GRANT ALL PRIVILEGES ON DATABASE mytestdb to mytestuser;    
        list created databases
        \l
        Connect to it
        \c mytestdb        
    6.3 Now create custom config on server, same directory as your jar file
        add there db configuration
        after that edit cinep.service file to launch with custom config 
        ExecStart=/usr/bin/java -jar your jar file.jar --spring.config.location=config file name
        restart service
        
7 Jacoco and actuator
    7.1 Jacoco
    in build.gradle under plugins add 
    id 'jacoco'
    and also add this
    jacocoTestReport {
    	reports {
    		xml.enabled = false
    		csv.enabled = false
    		html.destination file("${buildDir}/jacocoHtml")
    	}
    }
    run gradlew build jacocoTestReport in terminal. Report should be under build->jacocoHtml
    7.2 Actuator
    add compile group: 'org.springframework.boot', name: 'spring-boot-starter-actuator', version: '2.2.1.RELEASE' dependency
    and you are good to go. when you launch application you can do /api/actuator/health to verify that it is working
    also, by default not every endpoint is available, you need to enable them in config file.
        
        
        
