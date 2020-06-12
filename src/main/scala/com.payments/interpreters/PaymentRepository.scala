package com.payments.interpreters

import com.payments.algebras.PaymentRepositoryAlg
import com.payments.domain.Payment
import cats.effect.IO
import doobie.util.transactor.Transactor
import doobie._
import doobie.implicits._
import java.util.UUID

final class PaymentRepository(transactor: Transactor[IO]) extends PaymentRepositoryAlg[Payment] {
  def create(payment: Payment): IO[Payment] =
    (for {
      id     <- IO(UUID.randomUUID())
      result <- PaymentRepository.insert(id, payment).run.transact(transactor)
    } yield result) *> IO.pure(payment)
}

object PaymentRepository {
  def apply(transactor: Transactor[IO]): PaymentRepository = new PaymentRepository(transactor)

  private def insert(id: UUID, payment: Payment) =
    sql"insert payments (id, name, amount, recipient) values (${id.toString}, ${payment.name.value}, ${payment.amount.value}, ${payment.recipient.value})".update
}
