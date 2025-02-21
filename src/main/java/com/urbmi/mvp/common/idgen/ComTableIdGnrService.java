package com.urbmi.mvp.common.idgen;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author nive
 * @class ComTableIdGnrService
 * @desc ID를 생성하는 서비스 구현
 * TB_SEQ 안에 NAME / SEQ 로 각 테이블마다 시퀀스를 관리하는 형태로 구현
 * @since 2025-01-16
 */
public class ComTableIdGnrService {
    private DataSource dataSource;
    private String strategy;   // ID 앞에 붙일 접두사
    private String tableName;   // 테이블 이름 (ID 생성 기준 테이블)
    private int blockSize;      // ID 포맷을 위한 블록 사이즈

    // Setter methods for dataSource, strategy, tableName, blockSize
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    // ID 생성 메서드
    public String getNextId() {
        // 시퀀스 값 가져오기
        String query = "SELECT SEQ FROM TB_SEQ WHERE NAME = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, tableName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int seq = rs.getInt("SEQ");

                    // 값을 증가시키고 업데이트
                    String updateSeqValueSql = "UPDATE TB_SEQ SET SEQ = ? WHERE NAME = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateSeqValueSql)) {
                        updateStmt.setInt(1, seq + 1);
                        updateStmt.setString(2, tableName);
                        updateStmt.executeUpdate();
                    }

                    // blockSize를 고려하여 ID의 길이를 맞추는 로직
                    String formattedId = String.format("%0" + blockSize + "d", seq);

                    // strategy가 있을 경우 접두사 추가
                    if (strategy != null && !strategy.isEmpty()) {
                        return strategy + formattedId;
                    }

                    return formattedId; // 기본 ID
                } else {
                    // `NAME`이 없으면 INSERT 처리
                    String insertSeqValueSql = "INSERT INTO TB_SEQ (NAME, SEQ) VALUES (?, ?)";
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertSeqValueSql)) {
                        insertStmt.setString(1, tableName);
                        insertStmt.setInt(2, 1); // 첫 번째 ID는 1로 시작
                        insertStmt.executeUpdate();
                    }

                    // 첫 번째 ID 생성
                    String formattedId = String.format("%0" + blockSize + "d", 1);

                    // strategy가 있을 경우 접두사 추가
                    if (strategy != null && !strategy.isEmpty()) {
                        return strategy + formattedId;
                    }

                    return formattedId; // 기본 ID
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get next ID", e);
        }
    }
}
