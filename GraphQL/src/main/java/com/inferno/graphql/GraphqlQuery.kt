package com.inferno.graphql

import java.lang.StringBuilder


/**
 * @author Inferno-Team
 * this Class Converter convert Object 2 GraphQL query as String object
 * */
object GraphqlQuery {
    private var builder: StringBuilder? = null

    /** This class To build GraphQL Query or Mutation Request*/
    class Builder {
        /**
         * we start with this function to create query request
         * */
        fun startQuery(): Builder {
            builder = StringBuilder()
            builder!!.append("{")
            return this
        }

        /**
         * we start with this function to create mutation request
         * */
        fun startMutation(): Builder {
            builder = StringBuilder()
            builder!!.append("mutation {")
            return this
        }

        /**
         * we end our request with this function rather it mutation or query
         * */
        fun end(): Builder {
            builder!!.append("\n}")
            return this
        }

        /**
         * @param queryName this parameter to to create query with it's name
         * @param selectedFields it's sortOf can get any parameters
         * but you need to pass String or Query object @see Query class
         * @param args if your query has parameters you will pass it here
         * or don't pass anything.
         * And your parameter need to be as QueryParameter object @see QueryParameter */
        fun addQuery(
            queryName: String?,
            selectedFields: Array<Any>,
            vararg args: QueryParameter
        ): Builder {
            builder!!.append("\n")
            builder!!.append(queryName)
            if (args.isNotEmpty()) builder!!.append(" ( ")
            for (i in args.indices) {
                val parameter = args[i]
                builder!!.append(parameter.key)
                    .append(" : \"")
                    .append(parameter.value)
                    .append("\"")
                if (i != args.size - 1) builder!!.append(" , ")
            }
            if (args.isNotEmpty())
                builder!!.append(" ) {").append("\n") else builder!!.append(" {")
                .append("\n")
            for (selectedField in selectedFields) {
                if (selectedField is Query) {
                    builder!!.append(selectedField.toString()).append("\n")
                } else {
                    builder!!.append(selectedField.toString()).append("\n")
                }
            }
            return end()
        }

        /**
         * @param mutationName this parameter to to create query with it's name
         * @param selectedFields it's sortOf can get any parameters
         * but you need to pass String or Query object @see Query class
         * @param args if your query has parameters you will pass it here
         * or don't pass anything.
         * And your parameter need to be as QueryParameter object @see QueryParameter */
        fun addMutation(
            mutationName: String?,
            selectedFields: Array<String?>,
            args: Array<QueryParameter>
        ): Builder {
            builder!!.append("\n")
            builder!!.append(mutationName).append(" ( ")
            for (i in args.indices) {
                val par = args[i]
                builder!!.append(par.key)
                    .append(" : ")
                    .append("\"")
                    .append(par.value)
                    .append("\"")
                if (i != args.size - 1) builder!!.append(" , ")
            }
            builder!!.append(" ) {").append("\n")
            for (field in selectedFields) builder!!.append("\t").append(field).append("\n")
            return end()
        }

        /**
         * This function with build our request and return it as String object*/
        fun build(): String {
            return builder.toString()
        }
    }

    /**
     * This class is representing inside Query and it may have its own Query
     * ( nested Query )*/
    class Query(
        private val fieldName: String,
        private val args: Array<QueryParameter>?,
        private val selectedFields: Array<Any>
    ) {
        override fun toString(): String {
            val builder = StringBuilder()
            builder.append(fieldName)
            if (args != null && args.isNotEmpty()) builder.append(" ( ")
            var i = 0
            while (args != null && i < args.size) {
                val parameter = args[i]
                builder.append(parameter.key)
                    .append(" : \"")
                    .append(parameter.value)
                    .append("\"")
                if (i != args.size - 1) builder.append(" , ")
                i++
            }
            if (args != null && args.isNotEmpty()) builder.append(" ) {")
                .append("\n") else builder.append(" {").append("\n")
            for (selectedField in selectedFields) {
                if (selectedField is Query) {
                    builder.append("\n").append(selectedField.toString()).append("\n")
                } else {
                    builder.append(selectedField.toString()).append("\n")
                }
            }
            builder.append(" }")
            return builder.toString()
        }
    }

    init {
        builder = StringBuilder()
    }
}