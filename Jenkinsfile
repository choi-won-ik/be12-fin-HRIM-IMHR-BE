pipeline {
    agent any
     stages {
        stage('SSH') {
            steps{
                script{
                    sshPublisher(
                        publishers: [
                            sshPublisherDesc(
                                configName: 'imhr-back-to-ec2',
                                verbose: true,
                                transfers: [
                                    // sshTransfer(
                                    //     execCommand: """

                                    //     """
                                    // ),
                                    sshTransfer(
                                        remoteDirectory: '/imhr',
                                        execCommand: """

                                            set -e  # 에러 발생 시 종료, 단 우리가 컨트롤 가능한 부분만 예외 처리

                                            export DB_PASSWORD=qwer1234;
                                            export DB_URL=jdbc:mariadb://imhr-mariadb.cr2826acokra.ap-northeast-2.rds.amazonaws.com:3306/imhr;
                                            export DB_USER=hrim;

                                            echo "[INFO] 클린 디렉토리"
                                            rm -rf be12-fin-HRIM-IMHR-BE || true

                                            echo "[INFO] Git 클론 시작"
                                            git clone https://github.com/beyond-sw-camp/be12-fin-HRIM-IMHR-BE.git

                                            cd ./be12-fin-HRIM-IMHR-BE
                                            chmod +x ./gradlew

                                            echo "[INFO] Gradle 빌드"
                                            ./gradlew bootJar || { echo '[ERROR] Gradle 빌드 실패'; exit 1; }

                                            JAR_PATH="./build/libs/be12-HRIM-IMHR-BE-0.0.1-SNAPSHOT.jar"

                                            echo "[INFO] 실행 중인 프로세스 확인"
                                            PID=\$(pgrep -f "java -jar \$JAR_PATH") || true

                                            if [ ! -z "\$PID" ]; then
                                                echo "[INFO] 기존 프로세스 종료 시도 (\$PID)"
                                                kill -15 \$PID || true
                                                sleep 5
                                                if ps -p \$PID > /dev/null; then
                                                    echo "[WARN] 프로세스가 종료되지 않아 강제 종료"
                                                    kill -9 \$PID || true
                                                else
                                                    echo "[INFO] 프로세스 정상 종료됨"
                                                fi
                                            else
                                                echo "[INFO] 실행 중인 프로세스 없음"
                                            fi

                                            echo "[INFO] 애플리케이션 백그라운드 실행"
                                            nohup java -jar \$JAR_PATH > ../app.log 2>&1 &

                                            echo "[SUCCESS] 배포 완료"
                                            exit 0
                                        """
                                    )
                                ]
                            )
                        ]
                    )
                }
            }
        }
    }
}
