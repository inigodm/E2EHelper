package db

import properties.PropertyLoader
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

object DBSetup {


    fun givenEmptyPaymentInstrumentTable() {
        executeSql(BD_CONNECTION_URL, "delete from payment_instrument")
    }

    fun givenEmptyCustomerTable() {
        executeSql(BD_CONNECTION_URL, "delete from stripe_customer")
    }

    fun givenEmptyChargeTable() {
        executeSql(BD_CONNECTION_URL, "delete from charge")
    }

    fun givenEmptyRefundTable() {
        executeSql(BD_CONNECTION_URL, "delete from refund")
    }

    fun given(urlConnection: String) {

    }

    fun findInstrumentByUserIdAndPaymentIntentId(
        userId: Long,
        paymentId: String
    ): Map<String, String> {
        return DBSetup.given(BD_CONNECTION_URL)
            .`when`(
                "SELECT * FROM payment_instrument WHERE user_id = '" + userId +
                        "' and data @> '{\"payment_method_id\":\"" + paymentId + "\"}'"
            )
            .then()
            .first()
    }

    fun findPaymentInstrumentByUserId(userId: Long?): List<Map<String, String>> {
        return DBSetup.given(BD_CONNECTION_URL)
            .`when`(String.format("SELECT * FROM payment_instrument WHERE user_id = %s", userId))
            .then()
            .get()
    }

    fun findCustomerByStripeCustomerId(stripeCustomerId: String?): Map<String, String> {
        return DBSetup.given(BD_CONNECTION_URL)
            .`when`(String.format("SELECT * FROM stripe_customer WHERE stripe_customer_id = '%s'", stripeCustomerId))
            .then()
            .get().get(0)
    }

    fun findChargeById(chargeId: String?): Map<String, String> {
        return DBSetup.given(BD_CONNECTION_URL)
            .`when`(String.format("SELECT * FROM charge WHERE id = '%s'", chargeId))
            .then().get().get(0)
    }

    @Throws(SQLException::class)
    fun executeSql(
        dbConnectionString: String?,
        sql: String?
    ) {
        DriverManager.getConnection(dbConnectionString)
            .use { db -> db.createStatement().use { statement -> statement.executeUpdate(sql) } }
    }

    @Throws(SQLException::class)
    fun readSql(
        dbConnectionString: String?,
        sql: String?,
        lineReader: ResultSetConsumer
    ) {
        DriverManager.getConnection(dbConnectionString).use { db ->
            db.createStatement().use { statement ->
                statement.executeQuery(sql).use { resultSet ->
                    while (resultSet.next()) {
                        lineReader.accept(resultSet)
                    }
                }
            }
        }
    }

    @SneakyThrows
    fun givenCustomer(userId: Long, customerId: String) {
        executeSql(
            BD_CONNECTION_URL,
            "insert into stripe_customer (id, user_id, stripe_customer_id) values ('"
                    + UUID.randomUUID() + "'," + userId + ", '" + customerId + "')"
        )
    }

    @SneakyThrows
    fun givenCardPaymentInstrument(
        paymentInstrumentId: UUID,
        paymentEngineId: String?,
        metadata: Map<String?, Any?>?,
        userId: Long?
    ) {
        givenPaymentInstrument(paymentInstrumentId, userId, paymentEngineId, "card", metadata)
    }

    fun givenPayPalPaymentInstrument(
        paymentInstrumentId: UUID,
        userId: Long?,
        data: Map<String?, Any?>?
    ) {
        givenPaymentInstrument(paymentInstrumentId, userId, "stripe", "paypal", data)
    }

    @SneakyThrows
    fun givenPaymentInstrument(
        paymentInstrumentId: UUID,
        userId: Long?,
        paymentEngineId: String?,
        paymentMethod: String?,
        metadata: Map<String?, Any?>?
    ) {
        executeSql(
            BD_CONNECTION_URL, String.format(
                "INSERT INTO payment_instrument (id, payment_engine_id, payment_method, data, user_id) values" +
                        "('%s', '%s', '%s', '%s', %s)",
                paymentInstrumentId.toString(),
                paymentEngineId,
                paymentMethod,
                toJson(metadata),
                userId
            )
        )
    }

    @Throws(SQLException::class)
    fun givenCharge(
        chargeId: String?, status: String?, amount: Int,
        externalCustomerId: String?, paymentIntentId: String?
    ) {
        executeSql(
            BD_CONNECTION_URL, String.format(
                "INSERT INTO charge (id, status, amount, currency, external_customer_id, " +
                        "external_charge_intent_id, engine) VALUES ('%s', '%s', %s, '%s', '%s', '%s', '%s')",
                chargeId,
                status,
                amount,
                "EUR",
                externalCustomerId,
                paymentIntentId,
                "stripe"
            )
        )
    }

    @Throws(JsonProcessingException::class)
    private fun toJson(obj: Any?): String {
        return ObjectMapper().writeValueAsString(obj)
    }
}
