package com.github.jmlb23.marvel

sealed class Either<out L, out R> {
	data class Left<out L>(val left: L) : Either<L, Nothing>()
	data class Right<out R>(val right: R) : Either<Nothing, R>()

	companion object {
		fun <L> fromLeft(left: L): Either<L, Nothing> = Left(left)
		fun <R> fromRight(right: R): Either<Nothing, R> = Right(right)
		fun <L, R, R2> Either<L, R>.flatMap(f: (R) -> Either<L, R2>): Either<L, R2> = when (this) {
			is Left -> fromLeft(this.left)
			is Right -> f(this.right)
		}

		fun <R> Result<R>.toEither(): Either<Throwable, R> = if (isSuccess){ fromRight(getOrNull()!!) } else fromLeft(exceptionOrNull()!!)

	}

	fun <R2> map(f: (R) -> R2): Either<L, R2> = when (this) {
		is Left -> Left(this.left)
		is Right -> fromRight(f(this.right))
	}

	fun <L2> mapError(f: (L) -> L2): Either<L2, R> = when (this) {
		is Left -> fromLeft(f(this.left))
		is Right -> fromRight(this.right)
	}

	fun fold(onLeft: (L) -> Unit, onRight: (R) -> Unit) {
		when (this) {
			is Left -> onLeft(this.left)
			is Right -> onRight(this.right)
		}
	}
}