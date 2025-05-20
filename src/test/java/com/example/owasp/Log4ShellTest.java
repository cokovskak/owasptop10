package com.example.owasp;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
@Import(Log4ShellTest.Log4ShellConfig.class)
public class Log4ShellTest {

    @Autowired
    private List<Log4ShellService> servicesToTest;

    @Test
    public void testVulnerabilityPatched() throws Exception {

        CountDownLatch waitLatch = new CountDownLatch(1);
        AtomicInteger connectionAttemptCounter = new AtomicInteger();
        Thread listener = new Thread(() -> {
            try {
                ServerSocket socket = new ServerSocket(22345);
                while(true) {
                    waitLatch.countDown();
                    Socket connection = socket.accept();
                    connectionAttemptCounter.getAndIncrement();
                    connection.close();
                }
            }
            catch(IOException ex) {
                throw new IllegalStateException(ex);
            }
        });
        listener.start();
        waitLatch.await();

        servicesToTest.forEach(service -> service.testLog("${jndi:ldap://127.0.0.1:1389}"));

        Assertions.assertEquals(0, connectionAttemptCounter.get());
        // If you're not using lombok, change the 6 to 2
        Assertions.assertEquals(6, servicesToTest.size());

        listener.interrupt();

    }

    @Configuration
    @ComponentScan
    public static class Log4ShellConfig {

    }

    public interface Log4ShellService {
        void testLog(String arg);
    }

    @Component
    public static class Service1 implements Log4ShellService {

        private static final Logger logger = LogManager.getLogger("Test");

        @Override
        public void testLog(String arg) {
            logger.info("Test: " + arg);
        }
    }

    @Component
    public static class Service2 implements Log4ShellService {

        private static final Logger logger = LogManager.getLogger("Test");

        @Override
        public void testLog(String arg) {
            logger.info("Test: {}", arg);
        }
    }

    // Remove this class if you're not using lombok
    @Component
    @Slf4j
    public static class Service3 implements Log4ShellService {

        @Override
        public void testLog(String arg) {
            log.info("Test: {}", arg);
        }
    }

    // Remove this class if you're not using lombok
    @Component
    @Slf4j
    public static class Service4 implements Log4ShellService {

        @Override
        public void testLog(String arg) {
            log.info("Test: " + arg);
        }
    }

    // Remove this class if you're not using lombok
    @Component
    @Log4j2
    public static class Service5 implements Log4ShellService {

        @Override
        public void testLog(String arg) {
            log.info("Test: {}", arg);
        }
    }

    // Remove this class if you're not using lombok
    @Component
    @Log4j2
    public static class Service6 implements Log4ShellService {

        @Override
        public void testLog(String arg) {
            log.info("Test: " + arg);
        }
    }

}
