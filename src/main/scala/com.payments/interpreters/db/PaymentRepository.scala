package com.payments.interpreters.db

import com.payments.algebras.PaymentRepositoryAlg
import com.payments.domain._
import cats.effect.IO
import doobie.util.transactor.Transactor
import doobie._
import doobie.implicits._
import java.util.UUID
import com.payments.domain.db.PaymentData

final class PaymentRepository(transactor: Transactor[IO])
    extends PaymentRepositoryAlg[PaymentData] {
  def create(payment: Payment): IO[PaymentData] =
    (for {
      id     <- IO(UUID.randomUUID())
      _      <- PaymentRepository.insert(id, payment).run.transact(transactor)
      result <- PaymentRepository.select(id).unique.transact(transactor)
    } yield result)

  def update(amountUpdate: AmountUpdate): IO[PaymentData] =
    for {
      _ <-
        PaymentRepository
          .updateAmount(amountUpdate.id, amountUpdate.amount)
          .run
          .transact(transactor)
      result <- PaymentRepository.select(amountUpdate.id).unique.transact(transactor)

    } yield result

}

object PaymentRepository {
  def apply(transactor: Transactor[IO]): PaymentRepository = new PaymentRepository(transactor)

  /**
    * Needed in order to read/write a UUID to the database
    * This will convert the UUID into a string when persisting
    * and convert a String into a UUID when selecting
    */
  implicit val uuidMeta: Meta[UUID] = Meta[String].timap(UUID.fromString)(_.toString)

  private[db] def insert(id: UUID, payment: Payment) =
    sql"insert payments (id, name, amount, recipient) values (${id.toString}, ${payment.name.value}, ${payment.amount.value}, ${payment.recipient.value})".update

  private[db] def updateAmount(id: UUID, amount: Amount) =
    sql"update payments set amount = ${amount.value} where id = ${id.toString()}".update

  private[db] def select(id: UUID) =
    sql"select id, name, amount, recipient from payments where id = ${id.toString}"
      .query[PaymentData]
}
